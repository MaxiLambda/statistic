(ns statistic.db.utils
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as result-set]
            [statistic.db.connection :refer [db]]))

(defn execute!
  "wrapper around jdbc/execute. removes table names before qualified maps"
  ([params ops]
   (jdbc/execute! db params (merge {:builder-fn result-set/as-unqualified-maps} ops)))
  ([params]
   (execute! params {})))

(defn execute-one!
  "wrapper around jdbc/execute-one!. removes table names before qualified maps"
  ([params ops]
   (jdbc/execute-one! db params (merge {:builder-fn result-set/as-unqualified-maps} ops)))
  ([params]
   (execute! params {})))