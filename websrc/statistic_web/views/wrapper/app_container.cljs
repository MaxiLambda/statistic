(ns statistic-web.views.wrapper.app-container
  (:require [reagent-mui.material.box :refer [box]]))

(defn app-container
  "wrapper for all views"
  [children]
  [box
   {:sx {:pl 2
         :pr 2
         :pt 2}}
   children
   ])
