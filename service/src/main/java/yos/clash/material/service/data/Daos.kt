package yos.clash.material.service.data

fun ImportedDao(): ImportedDao {
    return Database.database.openImportedDao()
}

fun PendingDao(): PendingDao {
    return Database.database.openPendingDao()
}

fun SelectionDao(): SelectionDao {
    return Database.database.openSelectionProxyDao()
}

fun ProviderMoreInfoDao(): ProviderMoreInfoDao {
    return Database.database.openSubscriptionUserInfoDao()
}