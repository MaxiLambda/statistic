(ns statistic-web.views.app-header
  (:require [re-frame.core :as re-frame]
            [reagent-mui.material.app-bar :refer [app-bar]]
            [reagent-mui.material.box :refer [box]]
            [reagent-mui.material.button :refer [button]]
            [reagent-mui.material.toolbar :refer [toolbar]]
            [reagent-mui.material.typography :refer [typography]]
            [statistic-web.re-frame.global-events :as events]
            [statistic-web.re-frame.global-subs :as subs]))

(defn switch-view-button
  [view-key button-text current_view]
  [button
   {:variant  "contained"
    :color    (if (= current_view view-key) "secondary" "info")
    :on-click #(re-frame/dispatch [::events/path-change view-key])}
   button-text
   ])

(defn header [children]
  (let [view (:name @(re-frame/subscribe [::subs/view]))]
    [:div
     [box
      {:sx {:flex-grow 1}}
      [app-bar
       {:position "static"}
       [toolbar {:sx {:gap "10px"}}
        [typography
         {:component "div"
          :variant   "h6"
          :sx        {:flex-grow 1}}
         "Statistics"
         ]
        (switch-view-button :leaderboard "Leaderboard" view)
        (switch-view-button :archive "Archive" view)
        (switch-view-button :management "Create Match" view)
        ]
       ]
      ]
     children
     ]))

