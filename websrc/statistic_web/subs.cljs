(ns statistic-web.subs
  (:require
   [re-frame.core :as re-frame]))

;;Data Extraction form global State - STEP 4

;;subscription handler for the current view
(re-frame/reg-sub
 ::view
 (fn [db]
   (:view db)))

;;subscription handler for wins, only setup by :home at the moment
(re-frame/reg-sub
  ::wins
  (fn [db]
    (-> db :view :params :wins)))