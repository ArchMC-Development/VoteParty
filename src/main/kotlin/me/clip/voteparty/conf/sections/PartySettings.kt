package me.clip.voteparty.config.sections

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer.*
import me.clip.voteparty.config.objects.Command
import me.clip.voteparty.config.objects.Commands
import me.clip.voteparty.config.objects.RewardsPerEvent

internal object PartySettings : SettingsHolder
{
	
	@JvmField
	@Comment("The amount of votes needed for a party to occur")
	val VOTES_NEEDED: Property<Int> = newProperty("party.votes_needed", 50)
	
	@JvmField
	@Comment("The list of worlds where party rewards won't be given")
	val DISABLED_WORLDS: Property<List<String>> = newListProperty("party.disabled_worlds", "")
	
	@JvmField
	@Comment("Choose to allow offline votes count towards the party")
	val OFFLINE_VOTES: Property<Boolean> = newProperty("party.offline_votes", true)
	
	@JvmField
	@Comment("The amount of time (in seconds) the server will wait to start the party after the amount needed has been achieved")
	val START_DELAY: Property<Int> = newProperty("party.start_delay", 15)
	
	@JvmField
	@Comment("The amount of time (in seconds) the server will wait between executing reward commands")
	val COMMAND_DELAY: Property<Int> = newProperty("party.command_delay", 1)
	
	@JvmField
	@Comment("Configuration for chance rewards to be given during a party.", "Add as many commands as you want, set their chance, and choose the max amount a player can earn!")
	val REWARD_COMMANDS: Property<RewardsPerEvent> = newBeanProperty(RewardsPerEvent::class.java, "party.reward_commands", RewardsPerEvent(true, 1, listOf(Command(50, "eco give %player_name% 100"), Command(50, "give %player_name% DIAMOND 6"), Command(50, "give %player_name% IRON_INGOT 12"))))
	
	@JvmField
	@Comment("A list of rewards that will ALWAYS be given to a player during a party")
	val GUARANTEED_REWARDS: Property<Commands> = newBeanProperty(Commands::class.java, "party.guaranteed_rewards", Commands(true, listOf("eco give %player_name% 10", "give %player_name% STEAK 8")))
	
	@JvmField
	@Comment("Commands to be executed before a party is started")
	val PRE_PARTY_COMMANDS: Property<Commands> = newBeanProperty(Commands::class.java, "party.pre_party_commands", Commands(true, listOf("broadcast Party will start soon!")))
	
	@JvmField
	@Comment("Commands to be executed when a party has started")
	val PARTY_COMMANDS: Property<Commands> = newBeanProperty(Commands::class.java, "party.party_commands", Commands(true, listOf("broadcast A Vote Party has started!")))

}