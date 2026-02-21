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
import ru.asmelnikov.data.local.models.TeamInfoEntity

interface TeamInfoRealmOptions {

    suspend fun upsertTeamInfoFromRemoteToLocal(teamInfo: TeamInfoEntity)

    fun getTeamInfoFlowById(teamId: String): Flow<TeamInfoEntity?>

    suspend fun upsertMatchesFromRemoteToLocal(matches: MatchesEntity)

    fun getMatchesFlowById(teamId: String): Flow<MatchesEntity?>

    class RealmOptionsImpl(private val realm: Realm) : TeamInfoRealmOptions {

        override suspend fun upsertTeamInfoFromRemoteToLocal(teamInfo: TeamInfoEntity) {
            withContext(Dispatchers.IO) {
                realm.write {
                    copyToRealm(teamInfo, UpdatePolicy.ALL)
                }
            }
        }

        override fun getTeamInfoFlowById(teamId: String): Flow<TeamInfoEntity?> {
            return realm.query<TeamInfoEntity>("id == $0", teamId).asFlow().map { it.list.firstOrNull() }.flowOn(Dispatchers.IO)
        }

        override suspend fun upsertMatchesFromRemoteToLocal(matches: MatchesEntity) {
            withContext(Dispatchers.IO) {
                realm.write {
                    copyToRealm(matches, UpdatePolicy.ALL)
                }
            }
        }

        override fun getMatchesFlowById(teamId: String): Flow<MatchesEntity?> {
            return realm.query<MatchesEntity>("id == $0", teamId).asFlow().map { it.list.firstOrNull() }.flowOn(Dispatchers.IO)
        }
    }
}
