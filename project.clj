(defproject datbin "0.1.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [io.pedestal/pedestal.service "0.6.1"]
                 [io.pedestal/pedestal.jetty "0.6.1"]
                 [hiccup "2.0.0-RC2"]
                 [garden "1.3.10"]
                 [datalevin "0.8.21"]
                 [ch.qos.logback/logback-classic "1.2.10" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.35"]
                 [org.slf4j/jcl-over-slf4j "1.7.35"]
                 [org.slf4j/log4j-over-slf4j "1.7.35"]]
  :jvm-opts ["--add-opens" "java.base/java.nio=ALL-UNNAMED"
             "--add-opens" "java.base/sun.nio.ch=ALL-UNNAMED"]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  :profiles {:dev {:aliases {"run-dev" ["trampoline" "run" "-m" "server.server/run-dev"]}
                   :dependencies [[io.pedestal/pedestal.service-tools "0.6.1"]]}
             :uberjar {:aot [server.server]}}
  :uberjar-exclusions [#"sandbox"]
  :uberjar-name "server.jar"
  :main ^{:skip-aot true} server.server)