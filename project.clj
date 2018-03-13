(defproject meme-generator "0.1.0"
  :description "Meme generator"
  :url "https://github.com/evvo/meme-generator"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [compojure "1.6.0"]
                 [ring/ring-defaults "0.3.1"]
                 [com.cemerick/url "0.1.1"]
                 [me.raynes/fs "1.4.6"]
                 [hiccup "1.0.5"]
                 [net.mikera/imagez "0.12.0"]
                 [metis "0.3.3"]
                 [lein-light-nrepl "0.3.3"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler meme-generator.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}}
  :repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]})
