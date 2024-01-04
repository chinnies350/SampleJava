package com.demo.api.repository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import com.demo.api.Country;
import com.demo.api.ListTypeValues;
import com.demo.api.PostStudentResult;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;


public interface CountryRepository extends JpaRepository<Country, Integer> {
	
//	@Autowired
//    EntityManagerFactory entityManagerFactory= null;
	
	
	
	
	
	@Query(value =
		    "SELECT c.country_id, c.country_name, c.country_code, c.currency_symbol, c.currency_code, " +
		    "       c.max_phonenumber_length, c.default_country, " +
		    "       IFNULL((SELECT JSON_OBJECT('listTypeValueId', lv.list_type_value_id, " +
		    "                                  'list_type_value_name', lv.list_type_value_name, " +
		    "                                  'status', lv.`status` " +
		    "                           ) FROM list_type_values AS lv WHERE lv.list_type_value_id = c.country_status " +
		    "                           OR lv.list_type_value_name = :mode LIMIT 1), '[]') AS country_status " +
		    "FROM loc_country AS c " +
		    "LEFT JOIN list_type_values AS lv ON (lv.list_type_value_id = c.country_status) " +
		    "WHERE (:id IS NOT NULL AND c.country_id = :id) " +
		    "   OR (:status IS NOT NULL AND c.country_status = :status) " +
		    "   OR (:mode IS NOT NULL AND (lv.list_type_value_name = :mode OR :mode IS NULL)) " +
		    "   OR (:countryName IS NOT NULL AND c.country_name = :countryName)" +
		    "   OR (:id IS NULL AND :status IS NULL AND :mode IS NULL AND :countryName IS NULL)",
		    nativeQuery = true)
		List<Tuple> getCountry(
		        @Param("id") Integer id,
		        @Param("status") Integer status,
		        @Param("mode") String mode,
		        @Param("countryName") String countryName);
	
	
	
	@Modifying
	@Query(value =
	        "INSERT INTO loc_country (creation_date, created_by, country_code, country_name, currency_symbol, " +
	        "currency_code, default_country, max_phonenumber_length, country_status) " +
	        "SELECT NOW(), :createdBy, :countryCode, :countryName, :currencySymbol, :currencyCode, " +
	        "IFNULL(:defaultCountry, 1), :maxPhonenumberLength, IFNULL(:countryStatus, 1) FROM DUAL " +
	        "WHERE NOT EXISTS (" +
	        "    SELECT 1 FROM loc_country WHERE country_code = :countryCode OR country_name = :countryName" +
	        ");", nativeQuery = true)
	void insertCountry(
	        @Param("createdBy") String createdBy,
	        @Param("countryCode") String countryCode,
	        @Param("countryName") String countryName,
	        @Param("currencySymbol") String currencySymbol,
	        @Param("currencyCode") String currencyCode,
	        @Param("defaultCountry") Boolean defaultCountry,
	        @Param("maxPhonenumberLength") String maxPhonenumberLength,
	        @Param("countryStatus") Integer countryStatus
	);

	@Query(value =
	        "SELECT " +
	        "   CASE " +
	        "       WHEN ROW_COUNT() > 0 THEN 'Country Created Successfully'" +
	        "       WHEN EXISTS (SELECT 1 FROM loc_country WHERE country_code = :countryCode) THEN 'Country Code Already Exists'" +
	        "       WHEN EXISTS (SELECT 1 FROM loc_country WHERE country_name = :countryName) THEN 'Country Name Already Exists'" +
	        "       ELSE 'Unable To Create Country' " +
	        "   END AS outMessage, " +
	        "   CASE " +
	        "       WHEN ROW_COUNT() > 0 THEN 200 " +
	        "       WHEN EXISTS (SELECT 1 FROM loc_country WHERE country_code = :countryCode) THEN 409 " +
	        "       WHEN EXISTS (SELECT 1 FROM loc_country WHERE country_name = :countryName) THEN 409 " +
	        "       ELSE 400 " +
	        "   END AS outStatusCode;", nativeQuery = true)
	List<Object[]> getCountryInsertResult(
	        @Param("countryCode") String countryCode,
	        @Param("countryName") String countryName
	);
	
	
    
	
	@Modifying
	@Query(value =
	        "UPDATE loc_country " +
	        "SET " +
	        "   last_updated_date = NOW(), " +
	        "   last_updated_by = :lastUpdatedBy, " +
	        "   country_code = :countryCode," +
	        "   country_name = :countryName," +
	        "   currency_code = :currencyCode," +
	        "   default_country = :defaultyCountry, " +
	        "   max_phonenumber_length = :maxPhonenumberLength," +
	        "   currency_symbol = :currencySymbol, " +
	        "   country_status = :countryStatus " +	        
	        "WHERE " +
	        "   country_id = :countryId",
	        nativeQuery = true)
	int updateCountry(
	        @Param("lastUpdatedBy") String lastUpdatedBy,
	        @Param("countryCode") String countryCode,
	        @Param("countryName") String countryName,
	        @Param("currencyCode") String currencyCode,
	        @Param("defaultyCountry") Boolean defaultyCountry,
	        @Param("maxPhonenumberLength") String maxPhonenumberLength,
	        @Param("currencySymbol") String currencySymbol,
	        @Param("countryStatus") Integer countryStatus,
	        @Param("countryId") Integer countryId
	);

	@Query(value =
	        "SELECT " +
	        "   CASE " +
	        "       WHEN ROW_COUNT() > 0 THEN 'Country Updated Successfully' " +
	        "       WHEN NOT EXISTS (SELECT 1 FROM loc_country WHERE country_id = :countryId) THEN 'Country Id Not Found' " +
	        "       ELSE 'Unable To Update Country' " +
	        "   END AS outMessage, " +
	        "   CASE " +
	        "       WHEN ROW_COUNT() > 0 THEN 200 " +
	        "       WHEN NOT EXISTS (SELECT 1 FROM loc_country WHERE country_id = :countryId) THEN 404 " +
	        "       ELSE 400 " +
	        "   END AS outStatusCode",
	        nativeQuery = true)
	List<Object[]> getCountryUpdateResult(@Param("countryId") Integer countryId);
	
	@Modifying
	@Query(value =
	        "UPDATE loc_country " +
	        "SET " +
	        "   last_updated_date = NOW(), " +
	        "   country_status = :countryStatus " +
	        "WHERE " +
	        "   country_id = :countryId " ,
	        nativeQuery = true)
	int updateCountryStatus(
	        @Param("countryStatus") Integer countryStatus,
	        @Param("countryId") Integer countryId
	);

	@Query(value = 
		    "SELECT " +
		    "   CASE " +
		    "       WHEN ROW_COUNT() > 0 THEN 'Country Status Updated Successfully' " +
		    "       WHEN :countryId = 0 THEN 'Country Id Null' " +
		    "       WHEN NOT EXISTS (SELECT 1 FROM loc_country WHERE country_id = :countryId) THEN 'Country Id Not Found' " +
		    "       ELSE 'Unable To Update Country' " +
		    "   END AS outMessage, " +
		    "   CASE " +
		    "       WHEN ROW_COUNT() > 0 THEN 200 " +
		    "       WHEN :countryId = 0 THEN 409 " +
		    "       WHEN NOT EXISTS (SELECT 1 FROM loc_country WHERE country_id = :countryId) THEN 404 " +
		    "       ELSE 400 " +
		    "   END AS outStatusCode",
		    nativeQuery = true)
		List<Object[]> getCountryStatusResult(@Param("countryId") Integer countryId);
		
		
		// multiple Insert
		boolean existsByCountryCodeOrCountryName(String countryCode, String countryName);
		
		
//  multiple insert 		
//		List<Country> saveAll(Iterable<? extends Country> entities);
		
		@Modifying
	    @Query(value =
	    		"INSERT INTO loc_country (creation_date, created_by, country_code, country_name, currency_symbol, " +
	    		        "currency_code, default_country, max_phonenumber_length, country_status) " +
	    		        "VALUES (NOW(), :createdBy, :countryCode, :countryName, :currencySymbol, :currencyCode, " +
	    		        "IFNULL(:defaultCountry, 1), :maxPhonenumberLength, IFNULL(:countryStatus, 1));" +
	    		        "SET @countryId = (SELECT country_id FROM loc_country WHERE country_code = :countryCode AND country_name = :countryName);" +
	    		        "INSERT INTO loc_state (creation_date, created_by, state_code, state_name, state_tin, country_id, state_status) " +
	    		        "VALUES (NOW(), :createdBy, :stateCode, :stateName, :stateTin, @countryId, :stateStatus);" +
	    		        "SELECT @countryId AS countryId;", nativeQuery = true)
	    Integer insertCountryAndState(
	            @Param("createdBy") String createdBy,
	            @Param("countryCode") String countryCode,
	            @Param("countryName") String countryName,
	            @Param("currencySymbol") String currencySymbol,
	            @Param("currencyCode") String currencyCode,
	            @Param("defaultCountry") Boolean defaultCountry,
	            @Param("maxPhonenumberLength") String maxPhonenumberLength,
	            @Param("countryStatus") Integer countryStatus,
	            @Param("stateCode") String stateCode,
	            @Param("stateName") String stateName,
	            @Param("stateTin") String stateTin,
	            @Param("stateStatus") Integer stateStatus
	    );

		@Query(value =
		        "SELECT " +
		        "   IF(:countryId > 0, 'Data Added Successfully', 'Unable to Insert Data') AS outMessage, " +
		        "   IF(:countryId > 0, 200, 400) AS outStatusCode;", nativeQuery = true)
		List<Object[]> checkInsertStatus(@Param("countryId") Integer countryId);
	
		
//		@Modifying
//		@Query(value =
//		        "INSERT INTO loc_country (creation_date, created_by, country_code, country_name, currency_symbol, " +
//		        "currency_code, default_country, max_phonenumber_length, country_status) " +
//		        "SELECT NOW(), :createdBy, :countryCode, :countryName, :currencySymbol, :currencyCode, " +
//		        "IFNULL(:defaultCountry, 1), :maxPhonenumberLength, IFNULL(:countryStatus, 1) FROM DUAL " +
//		        "WHERE NOT EXISTS (" +
//		        "    SELECT 1 FROM loc_country WHERE country_code = :countryCode OR country_name = :countryName" +
//		        ");", nativeQuery = true)
//		void MultiinsertCountry(
//		        @Param("createdBy") List<String> createdBy,
//		        @Param("countryCode") List<String> countryCode,
//		        @Param("countryName") List<String> countryName,
//		        @Param("currencySymbol") List<String> currencySymbol,
//		        @Param("currencyCode") List<String> currencyCode,
//		        @Param("defaultCountry") List<Boolean> defaultCountry,
//		        @Param("maxPhonenumberLength") List<String> maxPhonenumberLength,
//		        @Param("countryStatus") List<Integer> countryStatus
//		);
//
//		@Query(value =
//		        "SELECT " +
//		        "   CASE " +
//		        "       WHEN ROW_COUNT() > 0 THEN 'Country Created Successfully'" +
//		        "       WHEN EXISTS (SELECT 1 FROM loc_country WHERE country_code = :countryCode) THEN 'Country Code Already Exists'" +
//		        "       WHEN EXISTS (SELECT 1 FROM loc_country WHERE country_name = :countryName) THEN 'Country Name Already Exists'" +
//		        "       ELSE 'Unable To Create Country' " +
//		        "   END AS outMessage, " +
//		        "   CASE " +
//		        "       WHEN ROW_COUNT() > 0 THEN 200 " +
//		        "       WHEN EXISTS (SELECT 1 FROM loc_country WHERE country_code = :countryCode) THEN 409 " +
//		        "       WHEN EXISTS (SELECT 1 FROM loc_country WHERE country_name = :countryName) THEN 409 " +
//		        "       ELSE 400 " +
//		        "   END AS outStatusCode;", nativeQuery = true)
//		List<Object[]> getMultiCountryInsertResult(
//		        @Param("countryCode") List<String> countryCode,
//		        @Param("countryName") List<String> countryName
//		);
		
//		@Modifying
//		@Query(value = "INSERT INTO loc_country (creation_date, created_by, country_code, country_name, currency_symbol, " +
//		        "currency_code, default_country, max_phonenumber_length, country_status) " +
//		        "SELECT NOW(), :createdBy, :countryCode, :countryName, :currencySymbol, :currencyCode, " +
//		        "COALESCE(:defaultCountry, 1), :maxPhonenumberLength, COALESCE(:countryStatus, 1) " +
//		        "ON DUPLICATE KEY UPDATE " +
//		        "country_code = VALUES(country_code), country_name = VALUES(country_name)", nativeQuery = true)
//		void insertMultipleCountries(
//		        @Param("createdBy") List<String> createdBy,
//		        @Param("countryCode") List<String> countryCodes,
//		        @Param("countryName") List<String> countryNames,
//		        @Param("currencySymbol") List<String> currencySymbols,
//		        @Param("currencyCode") List<String> currencyCodes,
//		        @Param("defaultCountry") List<Boolean> defaultCountries,
//		        @Param("maxPhonenumberLength") List<String> maxPhonenumberLengths,
//		        @Param("countryStatus") List<ListTypeValues> countryStatuses
//		);
		
//		@Modifying
//		@Query(value = "INSERT INTO loc_country (creation_date, created_by, country_code, country_name, currency_symbol, " +
//		        "currency_code, default_country, max_phonenumber_length, country_status) " +
//		        "SELECT NOW(), :createdBy, :countryCode, :countryName, :currencySymbol, :currencyCode, " +
//		        "IFNULL(:defaultCountry, 1), :maxPhoneNumberLength, IFNULL(:countryStatus, 1) FROM DUAL ;", nativeQuery = true)
//		void insertMultipleCountries(
//		        @Param("createdBy") List<String>  createdBy,
//		        @Param("countryCode") List<String> countryCode,
//		        @Param("countryName") List<String> countryName,
//		        @Param("currencySymbol") List<String> currencySymbol,
//		        @Param("currencyCode") List<String> currencyCode,
//		        @Param("defaultCountry") List<Boolean> defaultCountry,
//		        @Param("maxPhoneNumberLength") List<String> maxPhoneNumberLength,
//		        @Param("countryStatus") List<ListTypeValues> countryStatus
//		);



	
//	@Procedure(procedureName = "post_country")
//    void postCountry(
//        @Param("createdBy") String createdBy,
//        @Param("countryCode") String countryCode,
//        @Param("countryName") String countryName,
//        @Param("currencySymbol")String currencySymbol,
//        @Param("currencyCode")String currencyCode,
//        @Param("defaultCountry")Boolean defaultCountry,
//        @Param("maxPhoneNumberLength")String maxPhoneNumberLength,
//        @Param("countryStatus")Integer countryStatus,
//        @Param("out_message") String outMessage,
//        @Param("out_statusCode") Integer outStatusCode
//    );
		
		

}
