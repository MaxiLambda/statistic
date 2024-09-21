(ns statistic.json-writer.date-value-wrapper
  (:import (java.time LocalDateTime)))

(defn date-value-wrapper
  "creates a function that can be used as option :value-fn on clojure.data.json/write-str to add handling for LocalDateTime values
   accepts no parameters or a value-handler function to enable the usage of multiple wrappers"
  ([] (date-value-wrapper identity))
  ([value-handler]
   (fn [_key value]
     (if (instance? LocalDateTime value)
       (str value)
       (value-handler value)))))