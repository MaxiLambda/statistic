(ns statistic-web.subs.global-subs
  (:require
   [re-frame.core :as re-frame]))

;;Data Extraction form global State - STEP 4

;;subscription handler for the current view
(re-frame/reg-sub
 ::view
 (fn [db]
   (:view db)))