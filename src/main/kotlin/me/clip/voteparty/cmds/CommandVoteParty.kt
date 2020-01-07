package me.clip.voteparty.cmds

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import co.aikar.commands.annotation.Syntax
import co.aikar.commands.annotation.Values
import co.aikar.commands.bukkit.contexts.OnlinePlayer
import me.clip.voteparty.VoteParty
import me.clip.voteparty.base.ADMIN_PERM
import me.clip.voteparty.base.Addon
import me.clip.voteparty.base.display
import me.clip.voteparty.base.sendMessage
import me.clip.voteparty.config.sections.PartySettings
import me.clip.voteparty.messages.Messages
import org.bukkit.command.CommandSender

@CommandAlias("vp")
data class CommandVoteParty(private val voteParty: VoteParty) : BaseCommand(), Addon
{
	
	override val plugin = voteParty.plugin
	
	
	@Subcommand("addvote")
	@Syntax("<amount>")
	@CommandPermission(ADMIN_PERM)
	fun addVote(issuer: CommandIssuer, @Default("1") amount: Int)
	{
		if (amount <= 0)
		{
			sendMessage(prefix, issuer, Messages.ERROR__INVALID_NUMBER)
			return
		} else
		{
			party.votesHandler.addVote(amount)
			sendMessage(prefix, issuer, Messages.VOTES__VOTE_COUNTER_UPDATED)
		}
	}
	
	@Subcommand("givecrate")
	@CommandCompletion("@online")
	@Syntax("<player> <amount>")
	@CommandPermission(ADMIN_PERM)
	fun giveCrate(issuer: CommandIssuer, @Values("@online") target: OnlinePlayer, @Default("1") amount: Int)
	{
		if (amount <= 0)
		{
			sendMessage(prefix, issuer, Messages.ERROR__INVALID_NUMBER)
			return
		} else
		{
			sendMessage(prefix, issuer, Messages.CRATE__CRATE_GIVEN, target.player)
			target.player.inventory.addItem(party.partyHandler.buildCrate(amount))
			sendMessage(prefix, currentCommandManager.getCommandIssuer(target.player), Messages.CRATE__CRATE_RECEIVED)
		}
	}
	
	@Subcommand("setcounter")
	@Syntax("<amount>")
	@CommandPermission(ADMIN_PERM)
	fun setCounter(issuer: CommandIssuer, amount: Int)
	{
		if (amount <= 0)
		{
			sendMessage(prefix, issuer, Messages.ERROR__INVALID_NUMBER)
			return
		} else
		{
			party.conf().setProperty(PartySettings.VOTES_NEEDED, amount)
			sendMessage(prefix, issuer, Messages.VOTES__VOTES_NEEDED_UPDATED)
		}
	}
	
	@Subcommand("startparty")
	@CommandPermission(ADMIN_PERM)
	fun startParty(issuer: CommandIssuer)
	{
		party.partyHandler.startParty()
		sendMessage(prefix, issuer, Messages.PARTY__FORCE_START_SUCCESSFUL)
	}
	
	@Subcommand("giveparty")
	@CommandCompletion("@players")
	@Syntax("<player>")
	@CommandPermission(ADMIN_PERM)
	fun giveParty(issuer: CommandIssuer, @Values("@players") target: OnlinePlayer)
	{
		if (target.player.world.name in party.conf().getProperty(PartySettings.DISABLED_WORLDS))
		{
			sendMessage(prefix, issuer, Messages.ERROR__DISABLED_WORLD)
			return
		}
		
		voteParty.partyHandler.giveGuaranteedPartyRewards(target.player)
		voteParty.partyHandler.giveRandomPartyRewards(target.player)
		sendMessage(prefix, issuer, Messages.VOTES__PRIVATE_PARTY_GIVEN, target.player)
		sendMessage(prefix, currentCommandManager.getCommandIssuer(target.player), Messages.VOTES__PRIVATE_PARTY_RECEIVED)
	}
	
	@Subcommand("reload")
	@CommandPermission(ADMIN_PERM)
	fun reload(issuer: CommandIssuer)
	{
		voteParty.conf().reload()
		sendMessage(prefix, issuer, Messages.INFO__RELOADED)
	}
	
	@Subcommand("help")
	fun help(sender: CommandSender)
	{
		display(sender, currentCommandManager)
	}
	
	@Default
	fun default(issuer: CommandIssuer)
	{
		sendMessage(prefix, currentCommandIssuer, Messages.INFO__VOTES_NEEDED)
	}
	
}