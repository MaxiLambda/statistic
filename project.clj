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
                 [org.clojure/data.json "2.5.0"]]
  :main ^:skip-aot statistic.core
  :profiles {:uberjar {:aot :all}}
  :repl-options {:init-ns statistic.core})
