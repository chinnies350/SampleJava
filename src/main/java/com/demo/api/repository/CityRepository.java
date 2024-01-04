package com.demo.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import com.demo.api.City;
import com.demo.api.PostStudentResult;
import com.demo.api.message.CityResponseEntity;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;


public interface CityRepository extends JpaRepository<City, Integer>{
	
	
//	@Query(nativeQuery = true, value = "CALL GetCity(:id, :status, :mode)")
//    List<Tuple> getCity(
//    		@Param("id") Integer id, 
//    		@Param("status") Integer status,
//    		@Param("mode")String mode);
	
	@Query(nativeQuery = true, value =
		    "SELECT c.city_id, c.city_name, " +
		    "IFNULL((SELECT JSON_OBJECT('listTypeValueId', lv.list_type_value_id, " +
		    "'list_type_value_name', lv.list_type_value_name, " +
		    "'status', lv.`status` ) " +
		    "FROM list_type_values AS lv WHERE lv.list_type_value_id = c.city_status " +
		    "OR lv.list_type_value_name = :mode LIMIT 1), '[]') AS city_status, " +
		    "IFNULL((SELECT JSON_OBJECT('countryId', lc.country_id, " +
		    "'countryName', lc.country_name) " +
		    "FROM loc_country AS lc WHERE lc.country_id = c.country_id LIMIT 1), '[]') AS country_id, " +
		    "IFNULL((SELECT JSON_OBJECT('stateId', ls.state_id, " +
		    "'stateName', ls.state_name) " +
		    "FROM loc_state AS ls WHERE ls.state_id = c.state_id LIMIT 1), '[]') AS state, " +
		    "c.created_by, c.last_updated_by, c.creation_date, c.last_updated_date " +
		    "FROM loc_city AS c " +
		    "LEFT JOIN list_type_values AS lv ON (lv.list_type_value_id = c.city_status) " +
		    "WHERE (:id IS NOT NULL AND c.city_id = :id) " +
		    "   OR (:status IS NOT NULL AND c.city_status = :status) " +
		    "   OR (:mode IS NOT NULL AND (lv.list_type_value_name = :mode OR :mode IS NULL)) " +
		    "   OR (:cityName IS NOT NULL AND c.city_name = :cityName)" +
		    "   OR (:state IS NOT NULL AND c.state_id = :state)" +
		    "   OR (:id IS NULL AND :status IS NULL AND :mode IS NULL AND :cityName IS NULL AND :state IS NULL)")
		List<Tuple> getCity(
		    @Param("id") Integer id,
		    @Param("status") Integer status,
		    @Param("mode") String mode,
		    @Param("cityName") String cityName,
		    @Param("state") Integer state
		);
//	
//	 @Procedure(name = "GetCity")
//	    List<Tuple> getCity(@Param("id") Integer id, @Param("status") Integer status);



	
//	@Procedure(name = "GetCity")
//    List<City> getCity(@Param("id") Integer id, @Param("status") Integer status);
	
	@Modifying
	@Query(value =
	        "INSERT INTO loc_city(creation_date, created_by, city_name, city_status, country_id, state_id) " +
	                "SELECT " +
	                "   NOW(), " +
	                "   :createdBy, " +
	                "   :cityName, " +
	                "   :cityStatus, " +
	                "   :countryId, " +
	                "   :stateId " +
	                "FROM DUAL " +
	                "WHERE NOT EXISTS (SELECT 1 FROM loc_city WHERE city_name = :cityName)",
	        nativeQuery = true)
	void insertCity(
	        @Param("createdBy") String createdBy,
	        @Param("cityName") String cityName,
	        @Param("cityStatus") Integer cityStatus,
	        @Param("countryId") Integer country,
	        @Param("stateId") Integer state
	);

	@Query(value =
	        "SELECT " +
	                "   CASE " +
	                "       WHEN ROW_COUNT() > 0 THEN 'City Created Successfully' " +
	                "       WHEN EXISTS (SELECT 1 FROM loc_city WHERE city_name = :cityName) THEN 'City Name Already Exists' " +
	                "       ELSE 'Unable To Create City' " +
	                "   END AS outMessage, " +
	                "   CASE " +
	                "       WHEN ROW_COUNT() > 0 THEN 200 " +
	                "       WHEN EXISTS (SELECT 1 FROM loc_city WHERE city_name = :cityName) THEN 409 " +
	                "       ELSE 400 " +
	                "   END AS outStatusCode",
	        nativeQuery = true)
	List<Object[]> getCityResponse(@Param("cityName") String cityName);
	
	@Modifying
	@Query(value =
	        "UPDATE loc_city " +
	        "SET " +
	        "   last_updated_date = NOW(), " +
	        "   last_updated_by = :lastUpdatedBy, " +
	        "   city_name = :cityName," +
	        "   city_status = :cityStatus, " +
	        "   country_id = :countryId, " +
	        "   state_id = :stateId " +
	        "WHERE " +
	        "   city_id = :cityId",
	        nativeQuery = true)
	int updateCity(
	        @Param("lastUpdatedBy") String lastUpdatedBy,
	        @Param("cityName") String cityName,
	        @Param("cityStatus") Integer cityStatus,
	        @Param("countryId") Integer country,
	        @Param("stateId") Integer state,
	        @Param("cityId") Integer cityId
	);

	@Query(value =
	        "SELECT " +
	        "   CASE " +
	        "       WHEN ROW_COUNT() > 0 THEN 'City Updated Successfully' " +
	        "       WHEN NOT EXISTS (SELECT 1 FROM loc_city WHERE city_id = :cityId) THEN 'City Id Not Found' " +
	        "       ELSE 'Unable To Update City' " +
	        "   END AS outMessage, " +
	        "   CASE " +
	        "       WHEN ROW_COUNT() > 0 THEN 200 " +
	        "       WHEN NOT EXISTS (SELECT 1 FROM loc_city WHERE city_id = :cityId) THEN 404 " +
	        "       ELSE 400 " +
	        "   END AS outStatusCode",
	        nativeQuery = true)
	List<Object[]> getCityUpdateResult(@Param("cityId") Integer cityId);
	
	
	@Modifying
	@Query(value =
	        "UPDATE loc_city " +
	        "SET " +
	        "   last_updated_date = NOW(), " +
	        "   city_status = :cityStatus " +
	        "WHERE " +
	        "   city_id = :cityId " ,
	        nativeQuery = true)
	int updateCityStatus(
	        @Param("cityStatus") Integer cityStatus,
	        @Param("cityId") Integer cityId
	);

	@Query(value = 
		    "SELECT " +
		    "   CASE " +
		    "       WHEN ROW_COUNT() > 0 THEN 'City Status Updated Successfully' " +
		    "       WHEN :cityId = 0 THEN 'City Id Null' " +
		    "       WHEN NOT EXISTS (SELECT 1 FROM loc_city WHERE city_id = :cityId) THEN 'City Id Not Found' " +
		    "       ELSE 'Unable To Update City' " +
		    "   END AS outMessage, " +
		    "   CASE " +
		    "       WHEN ROW_COUNT() > 0 THEN 200 " +
		    "       WHEN :cityId = 0 THEN 409 " +
		    "       WHEN NOT EXISTS (SELECT 1 FROM loc_city WHERE city_id = :cityId) THEN 404 " +
		    "       ELSE 400 " +
		    "   END AS outStatusCode",
		    nativeQuery = true)
		List<Object[]> getCityStatusResult(@Param("cityId") Integer cityId);
		
		
		@Modifying
		@Query(value =
		    "UPDATE loc_city " +
		    "SET " +
		    "   last_updated_date = NOW(), " +
		    "   city_status = (SELECT list_type_value_id FROM list_type_values WHERE list_type_value_name = :listTypeValueName  LIMIT 1) " +
		    "WHERE " +
		    "   city_id = :cityId",
		    nativeQuery = true)
		int updateCitylistTypeValueName(
		    @Param("listTypeValueName") String listTypeValueName,
		    @Param("cityId") Integer cityId
		);

		@Query(value = 
			    "SELECT " +
			    "   CASE " +
			    "       WHEN ROW_COUNT() > 0 THEN 'City Status Updated Successfully' " +
			    "       WHEN :cityId = 0 THEN 'City Id Null' " +
			    "       WHEN NOT EXISTS (SELECT 1 FROM loc_city WHERE city_id = :cityId) THEN 'City Id Not Found' " +
			    "       ELSE 'Unable To Update City' " +
			    "   END AS outMessage, " +
			    "   CASE " +
			    "       WHEN ROW_COUNT() > 0 THEN 200 " +
			    "       WHEN :cityId = 0 THEN 409 " +
			    "       WHEN NOT EXISTS (SELECT 1 FROM loc_city WHERE city_id = :cityId) THEN 404 " +
			    "       ELSE 400 " +
			    "   END AS outStatusCode",
			    nativeQuery = true)
			List<Object[]> getCitylistTypeNameResult(@Param("cityId") Integer cityId);

	
	

//	@Modifying
//	@Query(value = 
//	    "DECLARE rowCount INT; " +
//	    "SELECT COUNT(*) INTO rowCount FROM loc_city WHERE city_name = :cityName; " +
//	    "IF rowCount = 0 THEN " +
//	    "    INSERT INTO loc_city(creation_date, created_by, city_name, city_status, country_id, state_id) " +
//	    "    VALUES (NOW(), :createdBy, :cityName, :cityStatus, :countryId, :stateId); " +
//	    "    IF ROW_COUNT() > 0 THEN " +
//	    "        SELECT 'City Created Successfully' AS outMessage, 200 AS outStatusCode; " +
//	    "    ELSE " +
//	    "        SELECT 'Unable To Create City' AS outMessage, 400 AS outStatusCode; " +
//	    "    END IF; " +
//	    "ELSE " +
//	    "    SELECT 'City Name Already Exists' AS outMessage, 409 AS outStatusCode; " +
//	    "END IF;", nativeQuery = true)
//	List<PostStudentResult> postCity(
//	    @Param("createdBy") String createdBy,
//	    @Param("cityName") String cityName,
//	    @Param("cityStatus") Integer cityStatus,
//	    @Param("countryId") Integer country,
//	    @Param("stateId") Integer state
//	);
	
//	@Transactional
//	@Modifying
//	@Query(nativeQuery = true, value = 
//		    "BEGIN " +
//		    "DECLARE rowCount INT; " +
//		    "START TRANSACTION; " +
//		    "SELECT COUNT(*) INTO rowCount FROM loc_city WHERE city_name = :cityName; " +
//		    "IF rowCount = 0 THEN " +
//		    "INSERT INTO loc_city(creation_date, created_by, city_name, city_status, country_id, state_id) " +
//		    "VALUES (NOW(), :createdBy, :cityName, :cityStatus, :countryId, :stateId); " +
//		    "IF ROW_COUNT() > 0 THEN " +
//		    "SET :out_message = 'City Created Successfully'; " +
//		    "SET :out_statusCode = 200; " +
//		    "ELSE " +
//		    "SET :out_message = 'Unable To Create City'; " +
//		    "SET :out_statusCode = 400; " +
//		    "ROLLBACK; " +
//		    "END IF; " +
//		    "ELSE " +
//		    "SET :out_message = 'City Name Already Exists'; " +
//		    "SET :out_statusCode = 409; " +
//		    "END IF; " +
//		    "COMMIT; " +
//		    "END")
//		void postCity(
//		    @Param("createdBy") String createdBy,
//		    @Param("cityName") String cityName,
//		    @Param("cityStatus") Integer cityStatus,
//		    @Param("countryId") Integer country,
//		    @Param("stateId") Integer state,
//		    @Param("out_message") String outMessage,
//		    @Param("out_statusCode") Integer outStatusCode
//		);

	
//	@Procedure(procedureName = "post_city")
//    void postCity(
//        @Param("createdBy") String createdBy,
//        @Param("cityName") String cityName,
//        @Param("cityStatus") Integer cityStatus,
//        @Param("countryId")Integer countryId,
//        @Param("stateId")Integer stateId,
//        @Param("out_message") String outMessage,
//        @Param("out_statusCode") Integer outStatusCode
//    );

}
