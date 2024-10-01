(ns statistic.db.tables.spaces
  (:require [statistic.db.utils :refer [execute! execute-one!]]))

(defn get-all []
  (execute! ["SELECT * FROM spaces"]))

(defn get-by-id [{id :id}]
  (first (execute-one! ["SELECT * FROM spaces WHERE spaces.id = ?" id])))

(defn create
  "creates a new player and returns {:id}"
  [{name    :name
    view-pw :view-pw
    edit-pw :edit-pw}]
  (execute-one! ["INSERT INTO spaces(name,view_password,edit_password)
                  VALUES (?,?,?) RETURNING id" name view-pw edit-pw]))