package com.fst.gestionpresencepointage.service;

import com.fst.gestionpresencepointage.dto.DashboardEnseignantDto;

public interface DashboardEnseignantService {
    DashboardEnseignantDto chargerDashboard(Long enseignantId);
}
