(ns statistic-web.subs
  (:require
   [re-frame.core :as re-frame]))

;;Data Extraction form global State - STEP 4

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
  ::table
  (fn [db]
    (:table db)))
