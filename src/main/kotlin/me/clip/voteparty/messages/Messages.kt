package me.clip.voteparty.messages

import co.aikar.locales.MessageKey
import co.aikar.locales.MessageKeyProvider

enum class Messages : MessageKeyProvider
{
	ERROR__INVALID_NUMBER,
	ERROR__DISABLED_WORLD,
	
	INFO__VOTES_NEEDED,
	INFO__RELOADED,
	INFO__PLAYER_VOTE_COUNT,
	INFO__VOTE_COUNT_RESET,
	
	PARTY__FORCE_START_SUCCESSFUL,
	
	VOTES__VOTES_NEEDED_UPDATED,
	VOTES__VOTE_COUNTER_UPDATED,
	VOTES__PRIVATE_PARTY_GIVEN,
	VOTES__PRIVATE_PARTY_RECEIVED,
	
	CRATE__CRATE_GIVEN,
	CRATE__CRATE_RECEIVED,
	
	DESCRIPTIONS__HELP,
	DESCRIPTIONS__SETCOUNTER,
	DESCRIPTIONS__ADDVOTE,
	DESCRIPTIONS__STARTPARTY,
	DESCRIPTIONS__GIVECRATE,
	DESCRIPTIONS__GIVEPARTY,
	DESCRIPTIONS__RELOAD,
	DESCRIPTIONS__CHECKVOTES,
	DESCRIPTIONS__RESETVOTES;
	
	private val key = MessageKey.of(name.toLowerCase().replace("__", ".").replace("_", "-"))
	
	override fun getMessageKey(): MessageKey?
	{
		return key
	}
}
