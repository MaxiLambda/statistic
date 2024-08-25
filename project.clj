(defproject statistic "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [http-kit/http-kit "2.8.0"]
                 [compojure "1.7.1"]
                 [com.github.seancorfield/next.jdbc "1.3.834"]
                 [org.postgresql/postgresql "42.2.10"]
                 [org.clojure/data.json "2.5.0"]
                 [ring/ring-core "1.12.2"]
                 [ring-basic-authentication/ring-basic-authentication "1.2.0"]
                 ;;cljs dependencies
                 [thheller/shadow-cljs "2.28.12"]
                 ;;if things break, maybe go back to version 1.1.1
                 [reagent "1.2.0"]
                 [re-frame "1.4.3"]
                 [binaryage/devtools "1.0.6"]
                 [re-com "2.21.21"]]
  :source-paths ["src", "websrc"]
  :main ^:skip-aot statistic.core
  :profiles {:uberjar {:aot :all}}
  :repl-options {:init-ns statistic.core}
  :ring {:handler statistic.rest.routes/app-routes}
  :plugins [[lein-ring "0.12.6"]])
