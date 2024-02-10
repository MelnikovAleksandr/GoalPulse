package ru.asmelnikov.data.local

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObjectChangeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ru.asmelnikov.data.local.models.CompetitionMatchesEntity
import ru.asmelnikov.data.local.models.CompetitionScorersEntity
import ru.asmelnikov.data.local.models.CompetitionStandingsEntity

interface StandingsRealmOptions {

    suspend fun upsertStandingsFromRemoteToLocal(standings: CompetitionStandingsEntity)

    suspend fun getStandingsFlowById(compId: String): Flow<CompetitionStandingsEntity>

    suspend fun upsertScorersFromRemoteToLocal(comp: CompetitionScorersEntity)

    suspend fun getScorersFlowById(compId: String): Flow<CompetitionScorersEntity>

    suspend fun upsertMatchesFromRemoteToLocal(comp: CompetitionMatchesEntity)

    suspend fun getMatchesFlowById(compId: String): Flow<CompetitionMatchesEntity>

    class RealmOptionsImpl(private val realmConfig: RealmConfiguration) : StandingsRealmOptions {
        override suspend fun upsertStandingsFromRemoteToLocal(standings: CompetitionStandingsEntity) {
            withContext(Dispatchers.IO) {
                val realm = Realm.getInstance(realmConfig)
                realm.executeTransaction { transition ->
                    transition.insertOrUpdate(standings)
                }
                realm.close()
            }
        }

        override suspend fun getStandingsFlowById(compId: String): Flow<CompetitionStandingsEntity> {
            return callbackFlow {
                val realm = Realm.getInstance(realmConfig)
                val competition: CompetitionStandingsEntity? =
                    realm.where(CompetitionStandingsEntity::class.java)
                        .equalTo("id", compId)
                        .findFirst()

                if (competition != null) {
                    val compEntity: CompetitionStandingsEntity =
                        realm.copyFromRealm(competition)

                    trySend(compEntity)

                    val listener =
                        RealmObjectChangeListener<CompetitionStandingsEntity> { updatedEntity, _ ->
                            val updateComp = realm.copyFromRealm(updatedEntity)
                            trySend(updateComp)
                        }

                    competition.addChangeListener(listener)

                    awaitClose {
                        competition.removeChangeListener(listener)
                        realm.close()
                    }
                } else {
                    send(CompetitionStandingsEntity())
                    close()
                    realm.close()
                }
            }.flowOn(Dispatchers.Main)
        }

        override suspend fun upsertScorersFromRemoteToLocal(comp: CompetitionScorersEntity) {
            withContext(Dispatchers.IO) {
                val realm = Realm.getInstance(realmConfig)
                realm.executeTransaction { transition ->
                    transition.insertOrUpdate(comp)
                }
                realm.close()
            }
        }

        override suspend fun getScorersFlowById(compId: String): Flow<CompetitionScorersEntity> {
            return callbackFlow {
                val realm = Realm.getInstance(realmConfig)
                val competition: CompetitionScorersEntity? =
                    realm.where(CompetitionScorersEntity::class.java)
                        .equalTo("id", compId)
                        .findFirst()

                if (competition != null) {
                    val compEntity: CompetitionScorersEntity =
                        realm.copyFromRealm(competition)

                    trySend(compEntity)

                    val listener =
                        RealmObjectChangeListener<CompetitionScorersEntity> { updatedEntity, _ ->
                            val updateComp = realm.copyFromRealm(updatedEntity)
                            trySend(updateComp)
                        }

                    competition.addChangeListener(listener)

                    awaitClose {
                        competition.removeChangeListener(listener)
                        realm.close()
                    }
                } else {
                    send(CompetitionScorersEntity())
                    close()
                    realm.close()
                }
            }.flowOn(Dispatchers.Main)
        }

        override suspend fun upsertMatchesFromRemoteToLocal(comp: CompetitionMatchesEntity) {
            withContext(Dispatchers.IO) {
                val realm = Realm.getInstance(realmConfig)
                realm.executeTransaction { transition ->
                    transition.insertOrUpdate(comp)
                }
                realm.close()
            }
        }

        override suspend fun getMatchesFlowById(compId: String): Flow<CompetitionMatchesEntity> {
            return callbackFlow {
                val realm = Realm.getInstance(realmConfig)
                val matches: CompetitionMatchesEntity? =
                    realm.where(CompetitionMatchesEntity::class.java)
                        .equalTo("id", compId)
                        .findFirst()

                if (matches != null) {
                    val matchEntity: CompetitionMatchesEntity =
                        realm.copyFromRealm(matches)

                    trySend(matchEntity)

                    val listener =
                        RealmObjectChangeListener<CompetitionMatchesEntity> { updatedEntity, _ ->
                            val updateMatch = realm.copyFromRealm(updatedEntity)
                            trySend(updateMatch)
                        }

                    matches.addChangeListener(listener)

                    awaitClose {
                        matches.removeChangeListener(listener)
                        realm.close()
                    }
                } else {
                    send(CompetitionMatchesEntity())
                    close()
                    realm.close()
                }
            }.flowOn(Dispatchers.Main)
        }
    }
}