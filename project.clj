(use '[clojure.java.shell :only (sh)])
(require '[clojure.string :as string])

(defn git-ref
  []
  (or (System/getenv "GIT_COMMIT")
      (string/trim (:out (sh "git" "rev-parse" "HEAD")))
      ""))

(defproject org.cyverse/kifshare "2.8.1-SNAPSHOT"
  :description "CyVerse Quickshare for iRODS"
  :url "https://github.com/cyverse-de/kifshare"

  :license {:name "BSD"
            :url "http://cyverse.org/sites/default/files/iPLANT-LICENSE.txt"}

  :manifest {"Git-Ref" ~(git-ref)}
  :uberjar-name "kifshare-standalone.jar"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [medley "0.5.5"]
                 [org.cyverse/clj-jargon "2.8.0"
                   :exclusions [[org.slf4j/slf4j-log4j12]
                                [log4j]]]
                 [org.cyverse/service-logging "2.8.0"]
                 [org.cyverse/clojure-commons "2.8.0"]
                 [org.cyverse/common-cli "2.8.0"]
                 [me.raynes/fs "1.4.6"]
                 [cheshire "5.5.0"
                   :exclusions [[com.fasterxml.jackson.dataformat/jackson-dataformat-cbor]
                                [com.fasterxml.jackson.dataformat/jackson-dataformat-smile]
                                [com.fasterxml.jackson.core/jackson-annotations]
                                [com.fasterxml.jackson.core/jackson-databind]
                                [com.fasterxml.jackson.core/jackson-core]]]
                 [slingshot "0.12.2"]
                 [compojure "1.5.0"]
                 [de.ubercode.clostache/clostache "1.4.0"]
                 [com.cemerick/url "0.1.1"]]

  :eastwood {:exclude-namespaces [:test-paths]
             :linters [:wrong-arity :wrong-ns-form :wrong-pre-post :wrong-tag :misplaced-docstrings]}

  :ring {:init kifshare.config/init
         :handler kifshare.core/app}

  :profiles {:dev     {:resource-paths ["build" "conf" "dev-resources"]
                       :dependencies [[midje "1.6.3"]]
                       :plugins [[lein-midje "2.0.1"]]}
             :uberjar {:aot :all}}

  :plugins [[jonase/eastwood "0.2.3"]
            [lein-ring "0.7.5"]]

  :main ^:skip-aot kifshare.core)
