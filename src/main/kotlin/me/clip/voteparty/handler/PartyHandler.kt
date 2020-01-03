package me.clip.voteparty.handler

import me.clip.voteparty.base.Addon
import me.clip.voteparty.base.color
import me.clip.voteparty.base.formMessage
import me.clip.voteparty.base.item
import me.clip.voteparty.base.name
import me.clip.voteparty.base.reduce
import me.clip.voteparty.base.runTaskLater
import me.clip.voteparty.base.runTaskTimer
import me.clip.voteparty.conf.ConfigVoteParty
import me.clip.voteparty.plugin.VotePartyPlugin
import me.clip.voteparty.version.EffectType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PartyHandler(override val plugin: VotePartyPlugin) : Addon
{
	private val conf: ConfigVoteParty
		get() = party.conf()
	
	
	fun giveRandomPartyRewards(player: Player)
	{
		val take = conf.party?.maxRewardsPerPlayer?.takeIf { it > 0 } ?: return
		val cmds = conf.party?.rewardCommands?.commands?.takeIf { it.isNotEmpty() } ?: return
		
		val iter = cmds.reduce(take).iterator()
		
		plugin.runTaskTimer(conf.party?.rewardCommands?.delay ?: 1L)
		{
			if (!iter.hasNext())
			{
				return@runTaskTimer cancel()
			}
			
			server.dispatchCommand(server.consoleSender, formMessage(player, iter.next().command))
		}
	}
	
	fun giveGuaranteedPartyRewards(player: Player)
	{
		executeCommands(conf.party?.guaranteedRewards?.enabled,
		                conf.party?.guaranteedRewards?.commands,
		                player)
	}
	
	fun runPrePartyCommands()
	{
		executeCommands(conf.party?.prePartyCommands?.enabled,
		                conf.party?.prePartyCommands?.commands)
	}
	
	fun runPartyCommands()
	{
		executeCommands(conf.party?.partyCommands?.enabled,
		                conf.party?.partyCommands?.commands)
	}
	
	fun runPartyStartEffects()
	{
		executeEffects(conf.effects?.party_start?.enabled,
		               conf.effects?.party_start?.effects,
		               server.onlinePlayers)
	}
	
	fun runPartyCommandEffects(player: Player)
	{
		executeEffects(conf.effects?.party_command_execute?.enabled,
		               conf.effects?.party_command_execute?.effects,
		               listOf(player))
	}
	
	
	fun buildCrate(amount: Int): ItemStack
	{
		return item(conf.crate?.material?.parseMaterial() ?: Material.CHEST, amount)
		{
			name = conf.crate?.name ?: "Vote Party Crate"
			lore = conf.crate?.lore?.map(::color)
		}
	}
	
	fun startParty()
	{
		runPrePartyCommands()
		
		plugin.runTaskLater((conf.party?.startDelay ?: 10L) * 20L)
		{
			runPartyCommands()
			runPartyStartEffects()
			
			server.onlinePlayers.forEach()
			{
				giveGuaranteedPartyRewards(it)
				giveRandomPartyRewards(it)
			}
		}
	}
	
	
	
	private fun executeCommands(enabled: Boolean?, cmds: Collection<String>?, player: Player? = null)
	{
		if (enabled == false)
		{
			return
		}
		
		cmds?.forEach()
		{
			server.dispatchCommand(server.consoleSender, if (player == null) it else formMessage(player, it))
		}
	}
	
	
	private fun executeEffects(enabled: Boolean?, effects: Collection<EffectType?>?, players: Collection<Player>)
	{
		if (enabled == false)
		{
			return
		}
		
		effects?.filterNotNull()?.forEach()
		{ effect ->
			players.forEach()
			{ player ->
				party.hook().display(effect, player.location, null)
			}
		}
	}
	
}