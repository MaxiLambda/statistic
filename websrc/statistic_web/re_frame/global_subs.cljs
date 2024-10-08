(ns statistic-web.re-frame.global-subs
  (:require
    [re-frame.core :as re-frame]))

;;Data Extraction form global State - STEP 4

;;subscription handler for the current view
(re-frame/reg-sub
  ::view
  (fn [db]
    (-> db :view :name)))

(re-frame/reg-sub
  ::current-space
  (fn [db]
    (-> db :space)))

(re-frame/reg-sub
  ::current-mode
  (fn [db]
    (-> db :mode)))