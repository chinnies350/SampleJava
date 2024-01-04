package com.demo.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.demo.api.State;

import jakarta.persistence.Tuple;

public interface StateRepository extends JpaRepository<State, Integer> {
	
	@Procedure(procedureName = "post_state")
    void postState(
        @Param("createdBy") String createdBy,
        @Param("stateCode") String stateCode,
        @Param("stateName") String stateName,
        @Param("stateTin")String stateTin,
        @Param("countryId")Integer countryId,
        @Param("stateStatus")Integer stateStatus,
        @Param("out_message") String outMessage,
        @Param("out_statusCode") Integer outStatusCode
    );
	
	@Query(value =
		    "SELECT s.state_id, s.state_code, s.state_name, s.state_tin, " +
		    "   IFNULL((SELECT JSON_OBJECT('countryId', c.country_id, " +
		    "                               'countryCode', c.country_code, " +
		    "                               'countryName', c.country_name, " +
		    "                               'currencySymbol', c.currency_symbol, " +
		    "                               'currencyCode', c.currency_code, " +
		    "                               'maxPhoneNumberLength', c.max_phonenumber_length) " +
		    "           FROM loc_country AS c WHERE c.country_id = s.country_id), '[]') AS country, " +
		    "   IFNULL((SELECT JSON_OBJECT('listTypeValueId', lv.list_type_value_id, " +
		    "                               'list_type_value_name', lv.list_type_value_name, " +
		    "                               'status', lv.`status` " +
		    "           ) FROM list_type_values AS lv WHERE lv.list_type_value_id = s.state_status " +
		    "               OR lv.list_type_value_name = :mode LIMIT 1), '[]') AS state_status " +
		    "FROM loc_state AS s " +
		    "LEFT JOIN list_type_values AS lv ON (lv.list_type_value_id = s.state_status) " +
		    "WHERE (:id IS NOT NULL AND s.state_id = :id) " +
		    "   OR (:status IS NOT NULL AND s.state_status = :status) " +
		    "   OR (:mode IS NOT NULL AND (lv.list_type_value_name = :mode OR :mode IS NULL)) " +
		    "   OR (:country IS NOT NULL AND s.country_id = :country AND lv.list_type_value_name = 'Active') " +
		    "   OR (:stateName IS NOT NULL AND s.state_name = :stateName) " +
		    "   OR (:id IS NULL AND :status IS NULL AND :mode IS NULL AND :country IS NULL AND :stateName IS NULL)",
		    nativeQuery = true)
		List<Tuple> getState(@Param("id") Integer id,
		                    @Param("status") Integer status,
		                    @Param("mode") String mode,
		                    @Param("country") Integer country,
		                    @Param("stateName") String stateName);



	@Query(value =
	"SELECT s.state_id, s.state_code, s.state_name, s.state_tin, " +
	"   IFNULL((SELECT JSON_OBJECT('countryId', c.country_id, " +
	"                               'countryCode', c.country_code, " +
	"                               'countryName', c.country_name, " +
	"                               'currencySymbol', c.currency_symbol, " +
	"                               'currencyCode', c.currency_code, " +
	"                               'maxPhoneNumberLength', c.max_phonenumber_length) " +
	"           FROM loc_country AS c WHERE c.country_id = s.country_id), '[]') AS country, " +
	"   IFNULL((SELECT JSON_OBJECT('listTypeValueId', lv.list_type_value_id, " +
	"                               'list_type_value_name', lv.list_type_value_name, " +
	"                               'status', lv.`status` " +
	"           ) FROM list_type_values AS lv WHERE lv.list_type_value_id = s.state_status ), '[]') AS state_status " +
	"FROM loc_state AS s " +
	"WHERE (:country IS NOT NULL AND :stateName IS NOT NULL AND s.country_id = :country AND s.state_name = :stateName) " ,
	nativeQuery = true)
	List<Tuple> getStateByCountryandStateName(
	                @Param("country") Integer country,
	                @Param("stateName") String stateName);
	
	@Modifying
	@Query(value =
	        "UPDATE loc_state " +
	        "SET " +
	        "   last_updated_date = NOW(), " +
	        "   last_updated_by = :lastUpdatedBy, " +
	        "   state_code = :stateCode," +
	        "   state_name = :stateName," +
	        "   state_tin = :stateTin," +
	        "   country_id = :countryId, " +
	        "   state_status = :stateStatus " +     
	        "WHERE " +
	        "   state_id = :stateId",
	        nativeQuery = true)
	int updateState(
	        @Param("lastUpdatedBy") String lastUpdatedBy,
	        @Param("stateCode") String stateCode,
	        @Param("stateName") String stateName,
	        @Param("stateTin") String stateTin,
	        @Param("countryId") Integer countryId,
	        @Param("stateStatus") Integer stateStatus,
	        @Param("stateId") Integer stateId
	);

	@Query(value =
	        "SELECT " +
	        "   CASE " +
	        "       WHEN ROW_COUNT() > 0 THEN 'State Updated Successfully' " +
	        "       WHEN NOT EXISTS (SELECT 1 FROM loc_state WHERE state_id = :stateId) THEN 'State Id Not Found' " +
	        "       ELSE 'Unable To Update State' " +
	        "   END AS outMessage, " +
	        "   CASE " +
	        "       WHEN ROW_COUNT() > 0 THEN 200 " +
	        "       WHEN NOT EXISTS (SELECT 1 FROM loc_state WHERE state_id = :stateId) THEN 404 " +
	        "       ELSE 400 " +
	        "   END AS outStatusCode",
	        nativeQuery = true)
	List<Object[]> getStateUpdateResult(@Param("stateId") Integer stateId);
	
	@Modifying
	@Query(value =
	        "UPDATE loc_state " +
	        "SET " +
	        "   last_updated_date = NOW(), " +
	        "   state_status = :stateStatus " +
	        "WHERE " +
	        "   state_id = :stateId " ,
	        nativeQuery = true)
	int updateStateStatus(
	        @Param("stateStatus") Integer stateStatus,
	        @Param("stateId") Integer stateId
	);

	@Query(value = 
		    "SELECT " +
		    "   CASE " +
		    "       WHEN ROW_COUNT() > 0 THEN 'State Status Updated Successfully' " +
		    "       WHEN :stateId = 0 THEN 'State Id Null' " +
		    "       WHEN NOT EXISTS (SELECT 1 FROM loc_state WHERE state_id = :stateId) THEN 'State Id Not Found' " +
		    "       ELSE 'Unable To Update State' " +
		    "   END AS outMessage, " +
		    "   CASE " +
		    "       WHEN ROW_COUNT() > 0 THEN 200 " +
		    "       WHEN :stateId = 0 THEN 409 " +
		    "       WHEN NOT EXISTS (SELECT 1 FROM loc_state WHERE state_id = :stateId) THEN 404 " +
		    "       ELSE 400 " +
		    "   END AS outStatusCode",
		    nativeQuery = true)
		List<Object[]> getStateStatusResult(@Param("stateId") Integer stateId);
		
	}
