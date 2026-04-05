package com.kiaev.admin.car;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCarService {

	private final AdminCarRepository adminCarRepository;

	// 목록
	public List<AdminCar> findAll() {
		return adminCarRepository.findAll();
	}

	// 상세
	public AdminCar findById(Long carId) {
		return adminCarRepository.findById(carId).orElseThrow(() -> new IllegalArgumentException("차량이 없습니다."));
	}

	// 등록
	public void register(AdminCar adminCar, MultipartFile[] carFiles) throws Exception {
		List<String> savedPaths = new ArrayList<>();

		String uploadDir = "C:/upload/car/";
		File uploadFolder = new File(uploadDir);

		// 업로드 폴더가 없으면 생성
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}

		for (MultipartFile file : carFiles) {
			if (!file.isEmpty()) {
				String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
				File dest = new File(uploadDir + fileName);
				file.transferTo(dest);
				savedPaths.add("/upload/car/" + fileName);
			}
		}

		if (savedPaths.size() > 0)
			adminCar.setCarMainImg(savedPaths.get(0));
		if (savedPaths.size() > 1)
			adminCar.setCarImg1(savedPaths.get(1));
		if (savedPaths.size() > 2)
			adminCar.setCarImg2(savedPaths.get(2));
		if (savedPaths.size() > 3)
			adminCar.setCarImg3(savedPaths.get(3));

		if (adminCar.getCarStatus() == null || adminCar.getCarStatus().isBlank()) {
			adminCar.setCarStatus("판매중");
		}

		adminCarRepository.save(adminCar);
	}

	public void edit(AdminCar adminCar, MultipartFile mainFile, MultipartFile img1File, MultipartFile img2File,
			MultipartFile img3File) throws Exception {

		AdminCar findCar = findById(adminCar.getCarId());

		findCar.setCarNumber(adminCar.getCarNumber());
		findCar.setCarName(adminCar.getCarName());
		findCar.setCarBrand(adminCar.getCarBrand());
		findCar.setCarType(adminCar.getCarType());
		findCar.setCarColor(adminCar.getCarColor());
		findCar.setCarPrice(adminCar.getCarPrice());
		findCar.setCarPriceDisplay(adminCar.getCarPriceDisplay());
		findCar.setBatteryCapacity(adminCar.getBatteryCapacity());
		findCar.setDrivingRange(adminCar.getDrivingRange());
		findCar.setFastChargeTime(adminCar.getFastChargeTime());
		findCar.setChargePercentInfo(adminCar.getChargePercentInfo());
		findCar.setCarContent(adminCar.getCarContent());
		findCar.setCarStatus(adminCar.getCarStatus());

		String uploadDir = "C:/upload/car/";
		File uploadFolder = new File(uploadDir);

		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}

		if (mainFile != null && !mainFile.isEmpty()) {
			String fileName = System.currentTimeMillis() + "_" + mainFile.getOriginalFilename();
			File dest = new File(uploadDir + fileName);
			mainFile.transferTo(dest);
			findCar.setCarMainImg("/upload/car/" + fileName);
		}

		if (img1File != null && !img1File.isEmpty()) {
			String fileName = System.currentTimeMillis() + "_" + img1File.getOriginalFilename();
			File dest = new File(uploadDir + fileName);
			img1File.transferTo(dest);
			findCar.setCarImg1("/upload/car/" + fileName);
		}

		if (img2File != null && !img2File.isEmpty()) {
			String fileName = System.currentTimeMillis() + "_" + img2File.getOriginalFilename();
			File dest = new File(uploadDir + fileName);
			img2File.transferTo(dest);
			findCar.setCarImg2("/upload/car/" + fileName);
		}

		if (img3File != null && !img3File.isEmpty()) {
			String fileName = System.currentTimeMillis() + "_" + img3File.getOriginalFilename();
			File dest = new File(uploadDir + fileName);
			img3File.transferTo(dest);
			findCar.setCarImg3("/upload/car/" + fileName);
		}

		adminCarRepository.save(findCar);
	}

	// 품절 처리
	public void soldOut(Long carId) {
		AdminCar findCar = findById(carId);
		findCar.setCarStatus("품절");
		adminCarRepository.save(findCar);
	}
}
