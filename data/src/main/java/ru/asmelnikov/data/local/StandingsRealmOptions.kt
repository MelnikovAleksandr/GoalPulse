package ru.asmelnikov.data.local

import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.asmelnikov.data.local.models.MatchesEntity
import ru.asmelnikov.data.local.models.CompetitionScorersEntity
import ru.asmelnikov.data.local.models.CompetitionStandingsEntity

interface StandingsRealmOptions {

    suspend fun upsertStandingsFromRemoteToLocal(standings: CompetitionStandingsEntity)

    fun getStandingsFlowById(compId: String): Flow<CompetitionStandingsEntity?>

    suspend fun upsertScorersFromRemoteToLocal(comp: CompetitionScorersEntity)

    fun getScorersFlowById(compId: String): Flow<CompetitionScorersEntity?>

    suspend fun upsertMatchesFromRemoteToLocal(matches: MatchesEntity)

    fun getMatchesFlowById(compId: String): Flow<MatchesEntity?>

    class RealmOptionsImpl(private val realm: Realm) : StandingsRealmOptions {
        override suspend fun upsertStandingsFromRemoteToLocal(standings: CompetitionStandingsEntity) {
            withContext(Dispatchers.IO) {
                realm.write {
                    copyToRealm(standings, UpdatePolicy.ALL)
                }
            }
        }

        override fun getStandingsFlowById(compId: String): Flow<CompetitionStandingsEntity?> {
            return realm.query<CompetitionStandingsEntity>("id == $0", compId).asFlow()
                .map { it.list.firstOrNull() }
                .flowOn(Dispatchers.IO)
        }

        override suspend fun upsertScorersFromRemoteToLocal(comp: CompetitionScorersEntity) {
            withContext(Dispatchers.IO) {
                realm.write {
                    copyToRealm(comp, UpdatePolicy.ALL)
                }
            }
        }

        override fun getScorersFlowById(compId: String): Flow<CompetitionScorersEntity?> {
            return realm.query<CompetitionScorersEntity>("id == $0", compId).asFlow()
                .map { it.list.firstOrNull() }
                .flowOn(Dispatchers.IO)
        }


        override suspend fun upsertMatchesFromRemoteToLocal(matches: MatchesEntity) {
            withContext(Dispatchers.IO) {
                realm.write {
                    copyToRealm(matches, UpdatePolicy.ALL)
                }
            }
        }

        override fun getMatchesFlowById(compId: String): Flow<MatchesEntity?> {
            return realm.query<MatchesEntity>("id == $0", compId).asFlow()
                .map { it.list.firstOrNull() }
                .flowOn(Dispatchers.IO)
        }
    }
}