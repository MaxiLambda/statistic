(ns statistic-web.views.management-view
  (:require [re-frame.core :as re-frame]
            [reagent-mui.material.text-field :refer [text-field]]
            [statistic-web.subs.management-subs :as subs]))

(defn management-view []
  (let [players @(re-frame/subscribe [::subs/players])]
    [text-field { :variant "standard"
                 :label "helo"
                 :default-value "hallo"}]))


;;to create a new player
;;name -> char(20)



;;to create a new match
;;date
;;winner 0,1,2
;;discipline char(20)
;;tag char(20)
;;team1 -> [id]
;;team2 -> [id]