(ns meme-generator.image-processing-test
  (:require [clojure.test :refer :all]
            [meme-generator.image-processing :refer :all]))

(def image-params
;;   Width, Height (image), Width, Height (text)
  [1000 500 200 100])

(deftest image-position
  (testing "center center"
    (is
      (= (apply positionings image-params)
         {:center-x 400
          :center-y 275
          :left 50
          :right 750
          :top 150
          :bottom 400})))

    (testing "center center position"
      (is
        (=
          (apply get-position-by-definition {:horizontal "center" :vertical "center"} image-params)
          {:position-x 400 :position-y 275})))

    (testing "top center position"
      (is
        (=
          (apply get-position-by-definition {:horizontal "center" :vertical "top"} image-params)
          {:position-x 400 :position-y 150})))

    (testing "top left position"
        (is
          (=
            (apply get-position-by-definition {:horizontal "left" :vertical "top"} image-params)
            {:position-x 50 :position-y 150})))

    (testing "top right position"
        (is
          (=
            (apply get-position-by-definition {:horizontal "right" :vertical "top"} image-params)
            {:position-x 750 :position-y 150}))))


