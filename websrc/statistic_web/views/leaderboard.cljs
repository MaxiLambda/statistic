(ns statistic-web.views.leaderboard
  (:require [re-frame.core :as re-frame]
            [reagent-mui.material.box :refer [box]]
            [statistic-web.subs.leaderboard-subs :as subs]))

;;this needs to be a function for re-frame/subscribe
(defn leaderboard [] (let [player-wins @(re-frame/subscribe [::subs/wins])
                    ;;sort players descending (wins) by swapping sign
                    sorted-players (sort-by #(-> % :count -) player-wins)]
                [box
                 {:sx {:pl 2
                       :pr 2
                       :pt 2}}
                 [:h1 "Leader Board (overall wins, add filter by tag)"]
                 (for [player sorted-players]
                   [:p
                    {:key (:id player)}
                    (str (:name player) ":") (:count player)])
                 ]))