(ns statistic-web.subs
  (:require
   [re-frame.core :as re-frame]))

;;Data Extraction form global State - STEP 4

(re-frame/reg-sub
 ::view
 (fn [db]
   (:view db)))

(re-frame/reg-sub
  ::name
  (fn [db]
    (:name db)))
