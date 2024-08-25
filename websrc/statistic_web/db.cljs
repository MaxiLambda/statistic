(ns statistic-web.db)

;;The initial global state
(def default-db
  {:name "re-frame"
   :table [{:id 1 :name "maxi" :wins "all"}
           {:id 2 :name "const" :wins "some"}
           {:id 3 :name "percy" :wins "miau"}]})
