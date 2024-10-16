(ns statistic-web.views.management.management-data
  (:require [reagent.core :as r]
            ["luxon" :refer [DateTime]]))

;;matches format required by endpoint
(defn initial-match-form []
  {:date       (.now DateTime)
   :winner     0
   :tag        nil
   :discipline nil                                          ;;nil  of an empty string to use the first value in ::subs/disciplines as default
   :team1      []
   :team2      []
   })

;;matches format required by endpoint
(defn initial-new-player [] {:name nil})

;;date -> date
;;winner -> 0,1,2
;;discipline -> char(20)
;;tag -> char(20)
;;team1 -> [id]
;;team2 -> [id]
(defonce match-form (r/atom (initial-match-form)))

;;name -> char(20)
(defonce new-player (r/atom (initial-new-player)))

;;add listener to print all state changes to the console
;;(add-watch match-form :change-listener (fn [_key _ref _old new] (println new)))
;;(add-watch new-player :change-listener (fn [_key _ref _old new] (println new)))

(defn match-form-valid
  "check if match-form is valid"
  [form]
  ;;only these parameters a checked, because all other fields are always in a valid state
  ;;applies ONLY if they are not changed by external sources
  (let [{:keys [team2 tag discipline team1]} form]
    (and
      (< 0 (count tag))
      (< 0 (count discipline))
      (< 0 (count team1))
      (< 0 (count team2)))))