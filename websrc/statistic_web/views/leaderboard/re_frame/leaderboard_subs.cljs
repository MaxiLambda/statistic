(ns statistic-web.views.leaderboard.re-frame.leaderboard-subs
  (:require [re-frame.core :as re-frame]))

;;subscription handler for wins, only setup by :leaderboard at the moment
(re-frame/reg-sub
  ::wins
  (fn [db]
    (-> db :view :params :wins)))

(re-frame/reg-sub
  ::disciplines
  (fn [db]
    (-> db :view :params :disciplines)))

(re-frame/reg-sub
  ::tags
  (fn [db]
    (-> db :view :params :tags)))