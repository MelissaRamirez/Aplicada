package com.venticas.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.venticas.data.ReportData;
import com.venticas.domain.ReportItem;

@Service
public class ReportBusiness {

	@Autowired
	ReportData reportData;

	public List<ReportItem> getBestClient() {
		return reportData.getBestClient();
	}
}
