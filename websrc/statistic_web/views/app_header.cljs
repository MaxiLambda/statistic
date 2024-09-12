(ns statistic-web.views.app-header
  (:require [re-frame.core :as re-frame]
            [reagent-mui.material.app-bar :refer [app-bar]]
            [reagent-mui.material.box :refer [box]]
            [reagent-mui.material.button :refer [button]]
            [reagent-mui.material.toolbar :refer [toolbar]]
            [reagent-mui.material.typography :refer [typography]]
            [statistic-web.events :as events]))

(defn header [children]
  [:div
   [box
    {:sx {:flex-grow 1}}
    [app-bar
     {:position "static"}
     [toolbar
      [typography
       {:component "div"
        :variant   "h6"
        :sx        {:flex-grow 1}}
       "Statistics"
       ]
      [button
       {:variant  "contained"
        :color    "secondary"
        :on-click #(re-frame/dispatch [::events/path-change :home])}
       "Leader Board"
       ]
      [button
       {:variant  "contained"
        :color    "primary"
        :on-click #(re-frame/dispatch [::events/path-change :management])}
       "Edit (Admin)"
       ]
      ]
     ]
    ]
   children
   ])