(ns meme-generator.config)

(def projectPath (str (.getCanonicalPath (clojure.java.io/file ".")) "/"))
(def imagesFolder (str projectPath "resources/images/"))
(def memesFolder (str projectPath "resources/memes/"))
(def main-title "Meme Generator")
(def text-padding 50)
(def max-width 800)

(def image-defaults {:image "pic.png"
                :text "enter text"
                :size 55
                :font "Arial"
                :font-color "#ffffff"
                :font-style "regular"
                :with-outline false
                :position-x 100
                :position-y 100})
(def text-outline-color "#000000")

(def allowed-form-keys [:image :text :font :size :font-style :with-outline :font-color :position-x :position-y])
