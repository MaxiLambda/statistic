(ns statistic-web.re-frame.initial-state)

;;The initial global state
(def default-db
  {:view {:name   :leaderboard
          :params {}}
   ;;TODO add :name to show it in header
   :space {:id  1}})
