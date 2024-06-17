(config
    (password-field
        :name           "bearerToken"
        :label          "Api Key"
        :placeholder    "Enter API token"
        :required       true)
)

(default-source 
    (http/get :base-url "https://aff-api.uppromote.com/api/v1"
                        (header-params "Accept" "application/json"))
                (auth/bearerToken)
                (paging/page-number :page-number-query-param-initial-value 1
                                    :page-number-query-param-name "page"
                                    :limit 100
                                    :limit-query-param-name "limit"
                )
                (error-handler
                        (when :status 400 
                              :message "Bad request")
                        (when :status 401 
                              :message "Unauthenticated"))
                ;; (downloadable)
                ;; (format/json :compression :GZIP)
)


(entity AFFILIATE
        ;; "List of Affiliates"
        (api-docs-url "https://aff-api.uppromote.com/docs")
        (source (http/get :url "/affiliates")
                (extract-path "data"))

        (fields
        id :id
        email
        first_name
        last_name
        status
        email_verified
        company
        address
        country
        city
        state
        zip_code :<= "zipcode"
        phone
        facebook
        youtube
        instagram
        twitter
        created_at_timestamp
        affiliate_link
        program_id
        program_name
        )

        (relate
            (contains-list-of CUSTOMER_FIELD :inside-prop "custom_fields")
            (contains-list-of COUPONS :inside-prop "coupons")
        )
)

(entity CUSTOMER_FIELD
    (fields
    ;; index :id :index
    value
    label
    key)
    (relate
    (needs AFFILIATE :prop "id")))

(entity COUPONS
    (fields
    id :id
    affiliate_id :id 
    coupon
    description
    created_timestamp)
    (relate
    (links-to AFILIATE :prop "id")))