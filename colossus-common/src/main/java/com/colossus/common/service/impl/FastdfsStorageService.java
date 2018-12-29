package com.colossus.common.service.impl;

import com.colossus.common.service.StorageService;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * FastDFS存储服务
 *
 */

public class FastdfsStorageService implements StorageService, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(FastdfsStorageService.class);

	private TrackerClient trackerClient;

	@Value("${storage.fastdfs.tracker_server}")
	private String trackerServer;

	@Override
	public String upload(byte[] data, String extName) {
		TrackerServer trackerServer = null;
		StorageServer storageServer = null;
		StorageClient1 storageClient = null;
		try {
			NameValuePair[] meta_list = null; // new NameValuePair[0];

			trackerServer = trackerClient.getConnection();
			if (trackerServer == null) {
				logger.error("getConnection return null");
			}
			storageServer = trackerClient.getStoreStorage(trackerServer);
			storageClient = new StorageClient1(trackerServer, storageServer);
			String fileid = storageClient.upload_file1(data, extName, meta_list);
			logger.debug("uploaded file <{}>", fileid);
			return fileid;
		} catch (Exception ex) {
			logger.error("Upload fail", ex);
			return null;
		} finally {
			closeServer(trackerServer, storageServer);
		}
	}

	@Override
	public int delete(String fileId) {
		// TODO Auto-generated method stub
		System.out.println("deleting ....");
		TrackerServer trackerServer = null;
		StorageServer storageServer = null;
		StorageClient1 storageClient1 = null;
		int index = fileId.indexOf('/');
		String groupName = fileId.substring(0, index);
		try {
			trackerServer = trackerClient.getConnection();
			if (trackerServer == null) {
				logger.error("getConnection return null");
			}
			storageServer = trackerClient.getStoreStorage(trackerServer, groupName);
			storageClient1 = new StorageClient1(trackerServer, storageServer);
			return storageClient1.delete_file1(fileId);
		} catch (Exception ex) {
			logger.error("Delete fail", ex);
			return 1;
		} finally {
			closeServer(trackerServer, storageServer);
		}
	}

	private void closeServer(TrackerServer trackerServer, StorageServer storageServer) {
		if (storageServer != null) {
			try {
				storageServer.close();
			} catch (IOException e) {
				logger.error("关闭storage server失败{}",e);
			}
		}
		if (trackerServer != null) {
			try {
				trackerServer.close();
			} catch (IOException e) {
				logger.error("关闭track server失败{}",e);
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		File confFile = File.createTempFile("fastdfs", ".conf");
		PrintWriter confWriter = new PrintWriter(new FileWriter(confFile));
		confWriter.println("tracker_server=" + trackerServer);
		confWriter.close();
		ClientGlobal.init(confFile.getAbsolutePath());
		confFile.delete();
		TrackerGroup trackerGroup = ClientGlobal.g_tracker_group;
		trackerClient = new TrackerClient(trackerGroup);

		logger.info("Init FastDFS with tracker_server : {}", trackerServer);

	}

}
