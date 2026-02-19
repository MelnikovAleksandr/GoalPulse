package ru.asmelnikov.data.local

import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.asmelnikov.data.local.models.CompetitionMatchesEntity
import ru.asmelnikov.data.local.models.CompetitionStandingsEntity
import ru.asmelnikov.data.local.models.TeamInfoEntity
import ru.asmelnikov.data.local.models.TeamMatchesEntity

interface TeamInfoRealmOptions {

    suspend fun upsertTeamInfoFromRemoteToLocal(teamInfo: TeamInfoEntity)

    fun getTeamInfoFlowById(teamId: String): Flow<List<TeamInfoEntity>>

    suspend fun upsertMatchesFromRemoteToLocal(matches: TeamMatchesEntity)

    fun getMatchesFlowById(teamId: String): Flow<List<TeamMatchesEntity>>

    class RealmOptionsImpl(private val realm: Realm) : TeamInfoRealmOptions {

        override suspend fun upsertTeamInfoFromRemoteToLocal(teamInfo: TeamInfoEntity) {
            withContext(Dispatchers.IO) {
                realm.write {
                    copyToRealm(teamInfo, UpdatePolicy.ALL)
                }
            }
        }

        override fun getTeamInfoFlowById(teamId: String): Flow<List<TeamInfoEntity>> {
            return realm.query<TeamInfoEntity>().asFlow().map { it.list }.flowOn(Dispatchers.IO)
        }

        override suspend fun upsertMatchesFromRemoteToLocal(matches: TeamMatchesEntity) {
            withContext(Dispatchers.IO) {
                realm.write {
                    copyToRealm(matches, UpdatePolicy.ALL)
                }
            }
        }

        override fun getMatchesFlowById(teamId: String): Flow<List<TeamMatchesEntity>> {
            return realm.query<TeamMatchesEntity>().asFlow().map { it.list }.flowOn(Dispatchers.IO)
        }
    }
}
