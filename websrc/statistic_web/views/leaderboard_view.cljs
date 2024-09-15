(ns statistic-web.views.leaderboard-view
  (:require [re-frame.core :as re-frame]
            [statistic-web.subs.leaderboard-subs :as subs]))

;;this needs to be a function for re-frame/subscribe
(defn leaderboard-view [] (let [player-wins @(re-frame/subscribe [::subs/wins])
                           ;;sort players descending (wins) by swapping sign
                           sorted-players (sort-by #(-> % :count -) player-wins)]
                       [:<>
                        [:h1 "Leader Board (overall wins, add filter by tag)"]
                        (for [player sorted-players]
                          [:p
                           {:key (:id player)}
                           (str (:name player) ":") (:count player)])
                        ]))