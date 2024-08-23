(ns statistic.db.setup
  (:require [next.jdbc.date-time :as date-time]
            [next.jdbc.result-set :as result-set])
  (:import (java.sql Array)))

(defn db-setup []
  "postgresql specific setup"
  ;;time objects from the db are resolved with the current locale
  (date-time/read-as-local)
  ;;sql arrays are returned as clojure arrays and vice versa
  (extend-protocol result-set/ReadableColumn
    Array
    (read-column-by-label [^Array v _]    (vec (.getArray v)))
    (read-column-by-index [^Array v _ _]  (vec (.getArray v)))))
