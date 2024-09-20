(ns statistic-web.views.management.re-frame.management-subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::players
  (fn [db]
    (-> db :view :params :players)))

(re-frame/reg-sub
  ::disciplines
  (fn [db]
    (-> db :view :params :disciplines)))

(re-frame/reg-sub
  ::tags
  (fn [db]
    (-> db :view :params :tags)))