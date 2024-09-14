(ns statistic-web.subs.leaderboard-subs
  (:require [re-frame.core :as re-frame]))

;;subscription handler for wins, only setup by :leaderboard at the moment
(re-frame/reg-sub
  ::wins
  (fn [db]
    (-> db :view :params :wins)))