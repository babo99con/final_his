const fs = require('fs');
const path = require('path');

const BASE = path.join('c:', 'dev', 'HMS', 'patient_service', 'src', 'main', 'java', 'kr', 'co', 'hospital', 'patients');
const RES = path.join('c:', 'dev', 'HMS', 'patient_service', 'src', 'main', 'resources');

const pkgMap = {
  'kr.co.hospital.patients.epic.patient.patient': 'kr.co.hospital.patients.patient',
  'kr.co.hospital.patients.epic.patient.family': 'kr.co.hospital.patients.patient',
  'kr.co.hospital.patients.epic.patient.flag': 'kr.co.hospital.patients.patient',
  'kr.co.hospital.patients.epic.patient.memo': 'kr.co.hospital.patients.patient',
  'kr.co.hospital.patients.epic.patient.restriction': 'kr.co.hospital.patients.patient',
  'kr.co.hospital.patients.epic.patient.statushistory': 'kr.co.hospital.patients.patient',
  'kr.co.hospital.patients.epic.patient.infohistory': 'kr.co.hospital.patients.patient',
  'kr.co.hospital.patients.epic.insurance.insurance': 'kr.co.hospital.patients.insurance',
  'kr.co.hospital.patients.epic.insurance.insurancehistory': 'kr.co.hospital.patients.insurance',
  'kr.co.hospital.patients.epic.consent.consent': 'kr.co.hospital.patients.consent'
};

const classMap = {
  'PatientFamilyEntity': 'FamilyEntity', 'PatientFamilyResDTO': 'FamilyResDTO',
  'PatientFamilyCreateReqDTO': 'FamilyCreateReqDTO', 'PatientFamilyRepository': 'FamilyRepository',
  'PatientFlagEntity': 'FlagEntity', 'PatientFlagResDTO': 'FlagResDTO', 'PatientFlagCreateReqDTO': 'FlagCreateReqDTO', 'PatientFlagUpdateReqDTO': 'FlagUpdateReqDTO',
  'PatientFlagMapper': 'FlagMapper', 'PatientFlagRepository': 'FlagRepository', 'PatientFlagService': 'FlagService', 'PatientFlagServiceImpl': 'FlagServiceImpl',
  'PatientFlagController': 'FlagController', 'PatientFlagExceptionHandler': 'FlagExceptionHandler', 'PatientFlagNotFoundException': 'FlagNotFoundException',
  'PatientFlagReqMapStruct': 'FlagReqMapStruct', 'PatientFlagResMapStruct': 'FlagResMapStruct',
  'PatientMemoEntity': 'MemoEntity', 'PatientMemoResDTO': 'MemoResDTO', 'PatientMemoCreateReqDTO': 'MemoCreateReqDTO', 'PatientMemoUpdateReqDTO': 'MemoUpdateReqDTO',
  'PatientMemoMapper': 'MemoMapper', 'PatientMemoRepository': 'MemoRepository', 'PatientMemoService': 'MemoService', 'PatientMemoServiceImpl': 'MemoServiceImpl',
  'PatientMemoController': 'MemoController', 'PatientMemoExceptionHandler': 'MemoExceptionHandler', 'PatientMemoNotFoundException': 'MemoNotFoundException',
  'PatientMemoReqMapStruct': 'MemoReqMapStruct', 'PatientMemoResMapStruct': 'MemoResMapStruct',
  'PatientRestrictionEntity': 'RestrictionEntity', 'PatientRestrictionResDTO': 'RestrictionResDTO', 'PatientRestrictionCreateReqDTO': 'RestrictionCreateReqDTO', 'PatientRestrictionUpdateReqDTO': 'RestrictionUpdateReqDTO',
  'PatientRestrictionMapper': 'RestrictionMapper', 'PatientRestrictionRepository': 'RestrictionRepository', 'PatientRestrictionService': 'RestrictionService', 'PatientRestrictionServiceImpl': 'RestrictionServiceImpl',
  'PatientRestrictionController': 'RestrictionController', 'PatientRestrictionExceptionHandler': 'RestrictionExceptionHandler', 'PatientRestrictionNotFoundException': 'RestrictionNotFoundException',
  'PatientRestrictionReqMapStruct': 'RestrictionReqMapStruct', 'PatientRestrictionResMapStruct': 'RestrictionResMapStruct',
  'PatientStatusHistoryEntity': 'StatusHistoryEntity', 'PatientStatusHistoryResDTO': 'StatusHistoryResDTO', 'PatientStatusHistoryCreateReqDTO': 'StatusHistoryCreateReqDTO', 'PatientStatusHistoryUpdateReqDTO': 'StatusHistoryUpdateReqDTO',
  'PatientStatusHistoryMapper': 'StatusHistoryMapper', 'PatientStatusHistoryRepository': 'StatusHistoryRepository', 'PatientStatusHistoryService': 'StatusHistoryService', 'PatientStatusHistoryServiceImpl': 'StatusHistoryServiceImpl',
  'PatientStatusHistoryController': 'StatusHistoryController', 'PatientStatusHistoryExceptionHandler': 'StatusHistoryExceptionHandler', 'PatientStatusHistoryNotFoundException': 'StatusHistoryNotFoundException',
  'PatientStatusHistoryReqMapStruct': 'StatusHistoryReqMapStruct', 'PatientStatusHistoryResMapStruct': 'StatusHistoryResMapStruct',
  'PatientInfoHistoryEntity': 'InfoHistoryEntity', 'PatientInfoHistoryResDTO': 'InfoHistoryResDTO', 'PatientInfoHistoryMapper': 'InfoHistoryMapper',
  'PatientInfoHistoryRepository': 'InfoHistoryRepository', 'PatientInfoHistoryService': 'InfoHistoryService', 'PatientInfoHistoryServiceImpl': 'InfoHistoryServiceImpl',
  'PatientInfoHistoryController': 'InfoHistoryController', 'PatientFamilyController': 'FamilyController'
};

function transform(content, targetPkg) {
  let s = content.replace(/package\s+[\w.]+;/, 'package ' + targetPkg + ';');
  for (const [oldName, newName] of Object.entries(classMap)) {
    s = s.replace(new RegExp('\\\\b' + oldName + '\\\\b', 'g'), newName);
  }
  for (const [oldPkg, newPkg] of Object.entries(pkgMap)) {
    s = s.replace(new RegExp(oldPkg.replace(/\./g, '\\.'), 'g'), newPkg);
  }
  return s;
}

function ensureDir(p) { if (!fs.existsSync(p)) fs.mkdirSync(p, { recursive: true }); }
function copyTransform(src, dst, targetPkg) {
  const c = fs.readFileSync(src, 'utf8');
  fs.writeFileSync(dst, transform(c, targetPkg), 'utf8');
}

// patient: from epic/patient/patient/* + family (merge), flag, memo, restriction, statushistory, infohistory
const patientSrc = [
  { src: path.join(BASE, 'epic', 'patient', 'patient'), dst: path.join(BASE, 'patient'), pkg: 'kr.co.hospital.patients.patient' },
  { src: path.join(BASE, 'epic', 'patient', 'family'), dst: path.join(BASE, 'patient'), pkg: 'kr.co.hospital.patients.patient' },
  { src: path.join(BASE, 'epic', 'patient', 'flag'), dst: path.join(BASE, 'patient'), pkg: 'kr.co.hospital.patients.patient' },
  { src: path.join(BASE, 'epic', 'patient', 'memo'), dst: path.join(BASE, 'patient'), pkg: 'kr.co.hospital.patients.patient' },
  { src: path.join(BASE, 'epic', 'patient', 'restriction'), dst: path.join(BASE, 'patient'), pkg: 'kr.co.hospital.patients.patient' },
  { src: path.join(BASE, 'epic', 'patient', 'statushistory'), dst: path.join(BASE, 'patient'), pkg: 'kr.co.hospital.patients.patient' },
  { src: path.join(BASE, 'epic', 'patient', 'infohistory'), dst: path.join(BASE, 'patient'), pkg: 'kr.co.hospital.patients.patient' }
];

function walkDir(dir, files = []) {
  if (!fs.existsSync(dir)) return files;
  for (const e of fs.readdirSync(dir)) {
    const full = path.join(dir, e);
    if (fs.statSync(full).isDirectory()) walkDir(full, files);
    else if (e.endsWith('.java')) files.push(full);
  }
  return files;
}

for (const { src, dst, pkg } of patientSrc) {
  if (!fs.existsSync(src)) continue;
  for (const f of walkDir(src)) {
    const rel = path.relative(src, f);
    const out = path.join(dst, rel);
    ensureDir(path.dirname(out));
    let skip = false;
    const bn = path.basename(f);
    if (bn === 'PatientFamilyService.java' || bn === 'PatientFamilyServiceImpl.java') skip = true;
    if (bn === 'PatientFamilyEntity.java') {
      copyTransform(path.join(BASE, 'epic', 'patient', 'patient', 'entity', 'PatientFamilyEntity.java'), path.join(dst, 'entity', 'FamilyEntity.java'), pkg);
      skip = true;
    }
    if (!skip) copyTransform(f, out, pkg);
  }
}
console.log('Patient package copied.');
