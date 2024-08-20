(ns statistic.db.connection
  (:require [next.jdbc :as jdbc]))

(def db-config
  {:dbtype                "postgresql"
   :dbname                "statistic_db"
   :host                  "localhost"
   :user                  "admin"
   :password              "admin"
   ;;https://github.com/seancorfield/next-jdbc/blob/develop/doc/tips-and-tricks.md#postgresql
   :reWriteBatchedInserts true})

(def db (jdbc/get-datasource db-config))
