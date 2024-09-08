(ns statistic-web.views
  (:require
    [re-frame.core :as re-frame]
    [reagent-mui.material.app-bar :refer [app-bar]]
    [reagent-mui.material.box :refer [box]]
    [reagent-mui.material.button :refer [button]]
    [reagent-mui.material.toolbar :refer [toolbar]]
    [reagent-mui.material.typography :refer [typography]]
    [statistic-web.events :as events]
    [statistic-web.subs :as subs]))

;;create views - STEP 5

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
       ;;TODO real header message
       "Hello-Header"
       ]
      [button
       {:variant  "contained"
        :color    "primary"
        ;;TODO create event to gain admin access and redirect to view
        :on-click #(re-frame/dispatch [::events/path-param-change :other])}
       "Edit (Admin)"
       ]
      ]
     ]
    ]
   children
   ])

;;this needs to be a function for re-frame/subscribe
(defn home [] (let [name (re-frame/subscribe [::subs/name])]
                [:div
                 [:h1
                  "Hello from " @name "!?"]
                 [button
                  {:variant  "contained"
                   :color    "primary"
                   :on-click #(re-frame/dispatch [::events/path-param-change :other])}
                  "Click me"
                  ]
                 ]))

(def something [:p "Some other text"])

(defn views [view-key]
  (view-key {:home  (home)
             :other something}))

(defn main-panel []
  (let [view (re-frame/subscribe [::subs/view])]
    (-> @view                                               ;;get the view as a key
        views                                               ;;resolve to the correct view
        header)))                                           ;;apply header to selected view
