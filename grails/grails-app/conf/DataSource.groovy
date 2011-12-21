dataSource {
    pooled = true
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        dataSource {
            pooled = true
            driverClassName = "org.postgresql.Driver"
            dialect = "net.kaleidos.hibernate.TableNameSequencePostgresDialect"
            dbCreate = "update" // one of 'create', 'create-drop','update'
            url = "jdbc:postgresql://127.0.0.1:5432/weather"
			loggingSql = true
			username = "weather"
			password = "weather"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:hsqldb:mem:testDb"
        }
    }
    production {
        dataSource {
            pooled = true
            driverClassName = "org.postgresql.Driver"
            dialect = "net.kaleidos.hibernate.TableNameSequencePostgresDialect"
            dbCreate = "update"
            url = "jdbc:postgresql://127.0.0.1:5432/weatherdb"
			loggingSql = true
			username = "weather"
			password = "VrpVUJi3"
        }
    }
}
