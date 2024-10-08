(ns statistic-web.views.app-header
  (:require [re-frame.core :as re-frame]
            [reagent-mui.material.app-bar :refer [app-bar]]
            [reagent-mui.material.box :refer [box]]
            [reagent-mui.material.button :refer [button]]
            [reagent-mui.material.drawer :refer [drawer]]
            [reagent-mui.material.list :refer [list]]
            [reagent-mui.material.list-item :refer [list-item]]
            [reagent-mui.material.list-item-button :refer [list-item-button]]
            [reagent-mui.material.toolbar :refer [toolbar]]
            [reagent-mui.material.typography :refer [typography]]
            [reagent.core :as r]
            [statistic-web.re-frame.global-events :as events]
            [statistic-web.re-frame.global-subs :as subs]
            [statistic-web.views.react-hooks.use-is-small-view :refer [size-dependent]]
            ["react" :as react]))

(def available-views [[:leaderboard "Leaderboard"]
                      [:archive "Archive"]
                      [:management "Create Match"]
                      [:login "Login"]])

;;definitions for the desktop app-header
(defn switch-view-button
  [view-key button-text current-view]
  [button
   {:key      view-key
    :variant  "contained"
    :color    (if (= current-view view-key) "secondary" "info")
    :on-click #(re-frame/dispatch [::events/path-change view-key])}
   button-text
   ])

(defn big-view-actions [view]
  [:<>
   (for [[key text] available-views]
     (switch-view-button key text view))])

;;definitions for the mobile app-header
(defonce drawer-open (r/atom false))

(defn switch-view-list-button [view-key text current-view]
  [list-item {:key view-key}
   [list-item-button {:selected (= current-view view-key)
                      :on-click #(do (re-frame/dispatch [::events/path-change view-key])
                                     (reset! drawer-open false))}
    text]
   ])

(defn small-view-actions [view]
  [:<>
   [button {:variant  "contained"
            :color    "secondary"
            :on-click #(reset! drawer-open true)}
    "Open Menu"
    ]
   [drawer {:anchor   "right"
            :open     @drawer-open
            :on-close #(reset! drawer-open false)}
    [list
     (for [[key text] available-views]
       (switch-view-list-button key text view))
     ]
    ]
   ])

;;the app-header
(defn header [children]
  (let [view @(re-frame/subscribe [::subs/view])]
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

        [size-dependent
         [big-view-actions view]
         [small-view-actions view]]
        ]
       ]
      ]
     children
     ]))

