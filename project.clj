(defproject statistic "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [http-kit/http-kit "2.8.0"]
                 [compojure "1.7.1"]
                 [com.github.seancorfield/next.jdbc "1.3.834"]
                 [org.postgresql/postgresql "42.2.10"]
                 [org.clojure/data.json "2.5.0"]
                 [ring/ring-core "1.12.2"]
                 [ring/ring-json "0.5.1"]
                 ;;cljs dependencies
                 [thheller/shadow-cljs "2.28.12"]
                 [cljs-ajax "0.8.4"]
                 [reagent "1.2.0"]
                 [re-frame "1.4.3"]
                 [day8.re-frame/http-fx "0.2.4"]
                 [binaryage/devtools "1.0.6"]
                 [arttuka/reagent-material-ui "5.11.12-0"]]
  :source-paths ["src", "websrc"]
  :main ^:skip-aot statistic.core
  :profiles {:uberjar {:aot :all}}
  :repl-options {:init-ns statistic.core}
  :ring {:handler statistic.rest.routes/app-routes
         :init statistic.db.setup/db-setup}
  :plugins [[lein-ring "0.12.6"]])
