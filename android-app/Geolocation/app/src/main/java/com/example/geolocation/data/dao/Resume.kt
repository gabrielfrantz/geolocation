package com.example.geolocation.data.dao

import android.content.ContentValues
import android.provider.BaseColumns
import com.example.geolocation.data.entities.Resume
import com.example.geolocation.data.migrations.Coordinates
import com.example.geolocation.data.migrations.Resumes
import com.example.geolocation.global.Geolocation.Companion.database
import com.example.geolocation.global.Geolocation.Companion.newResumeId

class Resume: Default<Resume> {
    override fun create(newObject: Resume) {
        val resumeValues = ContentValues().apply {
            put(Resumes.ResumesTable.COLUMN_START_DATE, newObject.getStartDate())
            put(Resumes.ResumesTable.COLUMN_ROUTE_ID, newObject.getRouteId())
        };

        newResumeId = database.insert(Resumes.ResumesTable.TABLE_NAME, null, resumeValues);
    }

    override fun update(newObject: Resume) {
        val resumeValues = ContentValues().apply {
            put(Resumes.ResumesTable.COLUMN_END_DATE, newObject.getEndDate())
        };

        database.update(
            Resumes.ResumesTable.TABLE_NAME,
            resumeValues,
            BaseColumns._ID + "=?",
            arrayOf(newResumeId.toString())
        );
    }

    override fun destroy(id: Int) {
        val query = StringBuilder()
        query.append(Resumes.ResumesTable.COLUMN_ROUTE_ID)
            .append(" = ")
            .append(id)

        database.delete(Resumes.ResumesTable.TABLE_NAME, query.toString(), null)
    }

    override fun get(): Resume {
        val query = StringBuilder()

        query.append("SELECT ")
            .append(BaseColumns._ID)
            .append(", ")
            .append(Resumes.ResumesTable.COLUMN_ROUTE_ID)
            .append(" FROM ")
            .append(Resumes.ResumesTable.TABLE_NAME)
            .append(" WHERE ")
            .append(Resumes.ResumesTable.COLUMN_END_DATE)
            .append(" IS NULL ")
            .append(" ORDER BY ")
            .append(BaseColumns._ID)
            .append(" DESC LIMIT 1 ");

        val resume = Resume();

        val cursor = database.rawQuery(query.toString(), null);
        if (cursor != null) {
            with(cursor) {
                while (moveToNext()) {
                    resume.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(BaseColumns._ID))))
                    resume.setRouteId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Resumes.ResumesTable.COLUMN_ROUTE_ID))))
                }
            }
        }
        cursor.close()

        return resume;
    }

    fun getResumeByRouteId(routeId: Int): ArrayList<Resume> {
        val query = StringBuilder()

        query.append("SELECT ")
            .append(BaseColumns._ID)
            .append(", ")
            .append(Resumes.ResumesTable.COLUMN_START_DATE)
            .append(", ")
            .append(Resumes.ResumesTable.COLUMN_END_DATE)
            .append(" FROM ")
            .append(Resumes.ResumesTable.TABLE_NAME)
            .append(" WHERE ")
            .append(Resumes.ResumesTable.COLUMN_ROUTE_ID)
            .append(" = ")
            .append(routeId)
            .append(" ORDER BY ")
            .append(BaseColumns._ID);

        val resumeList = ArrayList<Resume>();

        val cursor = database.rawQuery(query.toString(), null);
        if (cursor != null) {
            with(cursor) {
                while (moveToNext()) {
                    val resume = Resume();
                    resume.setId(Integer.parseInt(getString(getColumnIndex(BaseColumns._ID))));
                    resume.setStartDate(getString(getColumnIndex(Resumes.ResumesTable.COLUMN_START_DATE)));
                    val endDate = getString(getColumnIndex(Resumes.ResumesTable.COLUMN_END_DATE));
                    if (endDate != null) {
                        resume.setEndDate(endDate);
                    } else {
                        resume.setEndDate("");
                    }
                    resumeList.add(resume);
                }
            }
        }
        cursor.close()

        return resumeList;
    }

}