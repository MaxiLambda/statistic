(ns statistic-web.views.leaderboard.leaderboard-view
  (:require [re-frame.core :as re-frame]
            [reagent-mui.material.form-control :refer [form-control]]
            [reagent-mui.material.grid :refer [grid]]
            [reagent-mui.material.input-label :refer [input-label]]
            [reagent-mui.material.menu-item :refer [menu-item]]
            [reagent-mui.material.select :refer [select]]
            [reagent-mui.material.table :refer [table]]
            [reagent-mui.material.table-body :refer [table-body]]
            [reagent-mui.material.table-cell :refer [table-cell]]
            [reagent-mui.material.table-head :refer [table-head]]
            [reagent-mui.material.table-row :refer [table-row]]
            [reagent.core :as r]
            [statistic-web.re-frame.global-events :as global-events]
            [statistic-web.views.leaderboard.re-frame.leaderboard-events :as events]
            [statistic-web.views.leaderboard.re-frame.leaderboard-subs :as subs]))

(defn initial-modifiers [] {})

;;discipline -> char(20)
;;tag -> char(20)
(defonce modifiers (r/atom (initial-modifiers)))

(add-watch modifiers :change-listener (fn [_key _ref _old new]
                                        (println new)       ;;debug output
                                        ;;re-fetch wins
                                        (re-frame/dispatch [::events/fetch-wins (select-keys new [:discipline :tag])])))

(defn change-discipline [discipline]
  (let [do-reset (-> discipline nil?)]
    (do (reset! modifiers (merge (initial-modifiers) (when (not do-reset) {:discipline discipline})))
        (re-frame/dispatch [::global-events/dispatch-multi-ordered
                            [:dispatch [::events/clear-tags]]
                            [:dispatch [::events/fetch-tags (when (not do-reset) {:discipline discipline})]]]))))

(defn change-tag [tag]
  (if (nil? tag)
    (swap! modifiers dissoc :tag)
    (swap! modifiers assoc :tag tag)))

;;this needs to be a function for re-frame/subscribe
(defn leaderboard-view [] (let [player-wins @(re-frame/subscribe [::subs/wins])
                                ;;sort players descending (wins) by swapping sign
                                sorted-players (sort-by #(-> % :count -) player-wins)
                                disciplines @(re-frame/subscribe [::subs/disciplines])
                                tags @(re-frame/subscribe [::subs/tags])
                                tag-selection-visibility (if (-> @modifiers :discipline nil? not)
                                                           "visible"
                                                           "hidden")]
                            [:<>
                             [:h1 "Leaderboard"]
                             [:br]
                             [grid {:columns 2 :display "flex"}
                              [form-control {:fullWidth true
                                             :sx        {:pr 1}} ;;if select is not wrapped in its own form-control the label goes crazy
                               [input-label {:id "disciplines-label"
                                             :sx {:bgcolor "background.default"}}
                                "Discipline"
                                ]
                               [select {:labelId   "disciplines-label"
                                        :value     (or (:discipline @modifiers) "")
                                        :on-change #(->> % .-target .-value change-discipline)
                                        :variant   "outlined"}
                                (for [discipline disciplines]
                                  [menu-item {:value discipline :key discipline} discipline])
                                [menu-item {:value nil :key nil :sx {:color "grey"}} "Clear"]
                                ]
                               ]
                              [form-control {:fullWidth true
                                             :sx        {:pr         1
                                                         :visibility tag-selection-visibility}} ;;if select is not wrapped in its own form-control the label goes crazy
                               [input-label {:id "tags-label"
                                             :sx {:bgcolor "background.default"}}
                                "Tag"
                                ]
                               [select {:labelId   "tags-label"
                                        :value     (or (:tag @modifiers) "")
                                        :on-change #(->> % .-target .-value change-tag)
                                        :variant   "outlined"}
                                (for [tag tags]
                                  [menu-item {:value tag :key tag} tag])
                                [menu-item {:value nil :key nil :sx {:color "grey"}} "Clear"]
                                ]
                               ]
                              ]

                             [table
                              [table-head
                               [table-row
                                [table-cell "Player"]
                                [table-cell "Wins"]
                                ]
                               ]
                              [table-body
                               (for [player sorted-players] ;;for instead of map because hiccup has special handling for vectors
                                 [table-row {:key (:id player)}
                                  [table-cell (:name player)]
                                  [table-cell (:count player)]
                                  ])
                               ]
                              ]
                             ]))