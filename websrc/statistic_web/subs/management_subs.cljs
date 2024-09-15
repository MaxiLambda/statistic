(ns statistic-web.subs.management-subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::players
  (fn [db]
    (-> db :view :params :players)))