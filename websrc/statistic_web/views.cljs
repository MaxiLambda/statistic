(ns statistic-web.views
  (:require
    [re-frame.core :as re-frame]
    [reagent-mui.material.button :refer [button]]
    [statistic-web.subs :as subs]
    [statistic-web.events :as events]))

;;create views - STEP 5


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

(defn views [key]
  (key {:home  (home)
        :other something}))

(defn main-panel []
  (let [view (re-frame/subscribe [::subs/view])]
    (views @view)))
