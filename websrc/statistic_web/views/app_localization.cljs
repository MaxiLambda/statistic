(ns statistic-web.views.app-localization
  (:require [reagent-mui.x.localization-provider :refer [localization-provider]]
            ["@mui/x-date-pickers/AdapterLuxon" :refer [AdapterLuxon]]))


(defn app-localization [children]
  [localization-provider
   {:date-adapter AdapterLuxon}
   children
   ])