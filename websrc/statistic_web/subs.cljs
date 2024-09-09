(ns statistic-web.subs
  (:require
   [re-frame.core :as re-frame]))

;;Data Extraction form global State - STEP 4
;;todo split in view specific subs
(re-frame/reg-sub
 ::view
 (fn [db]
   (:view db)))

(re-frame/reg-sub
  ::wins
  (fn [db]
    (-> db :view :params :wins)))